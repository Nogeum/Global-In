package com.global.login.controller;

import com.global.login.dto.AttendanceSummaryDto;
import com.global.login.dto.LoginResponse;
import com.global.login.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;

@Controller
@RequestMapping("/employee/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    // 근태관리 페이지 이동
    @GetMapping
    public String attendancePage(HttpSession session, Model model) {
        LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        model.addAttribute("loginUser", loginUser);
        return "employee/attendance";
    }

    // 근태 데이터 요약 REST API
    @GetMapping("/api/summary")
    @ResponseBody
    public ResponseEntity<?> getSummary(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpSession session) {

        LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
        if (loginUser == null) return ResponseEntity.status(401).body("로그인이 필요합니다.");

        AttendanceSummaryDto summary = attendanceService.getAttendanceSummary(loginUser.getEmpNo(), date);
        return ResponseEntity.ok(summary);
    }

    // 출근 처리 API
    @PostMapping("/api/check-in")
    @ResponseBody
    public ResponseEntity<String> checkIn(HttpSession session) {
        LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
        if (loginUser == null) return ResponseEntity.status(401).body("로그인이 필요합니다.");

        try {
            attendanceService.checkIn(loginUser.getEmpNo(), "사무실", "WEB");
            return ResponseEntity.ok("출근 처리되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 퇴근 처리 API
    @PostMapping("/api/check-out")
    @ResponseBody
    public ResponseEntity<String> checkOut(HttpSession session) {
        LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
        if (loginUser == null) return ResponseEntity.status(401).body("로그인이 필요합니다.");

        try {
            attendanceService.checkOut(loginUser.getEmpNo());
            return ResponseEntity.ok("퇴근 처리되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}