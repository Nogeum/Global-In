package com.global.login.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "employee")
@Getter @Setter
@NoArgsConstructor
public class Employee {

    @Id
    @Column(name = "EMP_NO", length = 20)
    private String empNo; // 사원번호 (PK)

    @Column(name = "EMP_NAME", nullable = false, length = 50)
    private String empName; // 사원이름

    @Column(name = "DEPT_CODE", length = 20)
    private String deptCode; // 부서코드

    @Column(name = "POSITION", nullable = false, length = 30)
    private String position; // 직급

    @Column(name = "LOGIN_ID", nullable = false, unique = true, length = 30)
    private String loginId; // 로그인 아이디

    @Column(name = "LOGIN_PW", nullable = false, length = 100)
    private String loginPw; // 비밀번호

    @Column(name = "EMAIL", nullable = false, length = 50)
    private String email; // 이메일

    @Column(name = "PHONE", nullable = false, length = 20)
    private String phone; // 전화번호

    @Column(name = "HIRE_DATE", nullable = false)
    private LocalDate hireDate; // 입사일

    @Column(name = "ROLE", nullable = false, length = 10)
    private String role; // 'ADMIN', 'EMPLOYEE' 등 (기본값: EMPLOYEE)

    @Column(name = "EMP_STATUS", nullable = false, length = 10)
    private String empStatus; // 'ACTIVE', 'RESIGNED' 등 (기본값: ACTIVE)

    @Column(name = "REG_DATE", nullable = false)
    private LocalDate regDate; // 등록일
}