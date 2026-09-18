package com.global.login.dto;

import com.global.login.entity.Employee;
import lombok.Getter;


// 로그인 성공 후 세션이나 응답에 보낼 사원 간략 정보

@Getter
public class LoginResponse {
    private String empNo;
    private String empName;
    private String deptCode;
    private String position;
    private String role;

    public LoginResponse(Employee employee) {
        this.empNo = employee.getEmpNo();
        this.empName = employee.getEmpName();
        this.deptCode = employee.getDeptCode();
        this.position = employee.getPosition();
        this.role = employee.getRole();
    }
}