package com.global.login.controller;

import com.global.login.dto.LoginRequest;
import com.global.login.dto.LoginResponse;
import com.global.login.entity.Employee;
import com.global.login.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final EmployeeService employeeService;

    // 메인 루트 접속 시 로그인 페이지로 이동
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    // 로그인 페이지 리턴
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // 로그인 처리 API
    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> processLogin(@RequestBody LoginRequest request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            Employee employee = employeeService.login(request.getLoginId(), request.getLoginPw());

            // 세션에 로그인한 사원 정보 저장
            LoginResponse loginUser = new LoginResponse(employee);
            session.setAttribute("loginUser", loginUser);

            // ROLE 값에 따라 직원 대시보드(/employee/main) 또는 관리자 메인으로 이동
            String redirectUrl;
            if ("ADMIN".equalsIgnoreCase(employee.getRole())) {
                redirectUrl = "/admin/main";
            } else {
                redirectUrl = "/employee/main"; // 직원 대시보드로 이동
            }

            response.put("success", true);
            response.put("redirectUrl", redirectUrl);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}