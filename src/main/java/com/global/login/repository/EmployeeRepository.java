package com.global.login.repository;

import com.global.login.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    // LOGIN_ID로 사원 정보 조회
    Optional<Employee> findByLoginId(String loginId);
}