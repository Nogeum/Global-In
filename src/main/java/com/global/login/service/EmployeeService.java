package com.global.login.service;

import com.global.login.entity.Employee;
import com.global.login.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



// 비밀번호 검증 및 재직 상태(EMP_STATUS) 검사 로직을 포함


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee login(String loginId, String loginPw) {
        // 1. 아이디 존재 여부 확인
        Employee employee = employeeRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        // 2. 재직 상태 확인 (퇴사자 로그인 방지)
        if (!"ACTIVE".equalsIgnoreCase(employee.getEmpStatus())) {
            throw new IllegalStateException("로그인할 수 없는 계정 상태입니다. (퇴사/휴직)");
        }

        // 3. 비밀번호 일치 여부 확인
        // (추후 Spring Security 도입 시 PasswordEncoder.matches()로 변경 필요)
        if (!employee.getLoginPw().equals(loginPw)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return employee;
    }
}
