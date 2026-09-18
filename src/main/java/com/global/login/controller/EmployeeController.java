package com.global.login.controller;

import com.global.login.dto.DashboardResponseDto;
import com.global.login.dto.LoginResponse;
import com.global.login.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final DashboardService dashboardService;

    // 직원 메인(대시보드) 페이지 화면
    @GetMapping("/main")
    public String mainPage(HttpSession session, Model model) {
        LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        DashboardResponseDto dashboardData = dashboardService.getDashboardData(loginUser.getEmpNo(), loginUser.getEmpName());
        model.addAttribute("dashboard", dashboardData);
        model.addAttribute("loginUser", loginUser);

        return "employee/dashboard";
    }

    // 출근 버튼 클릭 처리
    @PostMapping("/api/check-in")
    @ResponseBody
    public ResponseEntity<String> checkIn(HttpSession session) {
        LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
        if (loginUser == null) return ResponseEntity.status(401).body("로그인이 필요합니다.");

        try {
            dashboardService.checkIn(loginUser.getEmpNo());
            return ResponseEntity.ok("출근 등록이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 퇴근 버튼 클릭 처리
    @PostMapping("/api/check-out")
    @ResponseBody
    public ResponseEntity<String> checkOut(HttpSession session) {
        LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
        if (loginUser == null) return ResponseEntity.status(401).body("로그인이 필요합니다.");

        try {
            dashboardService.checkOut(loginUser.getEmpNo());
            return ResponseEntity.ok("퇴근 등록이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}