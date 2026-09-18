package com.global.login.controller;

import com.global.login.dto.LoginResponse;
import com.global.login.dto.ScheduleDto;
import com.global.login.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/employee/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 일정관리 화면 리턴
    @GetMapping
    public String schedulePage(HttpSession session, Model model) {
        LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("loginUser", loginUser);
        return "employee/schedule";
    }

    // 특정 월 일정 목록 조회 REST API
    @GetMapping("/api/list")
    @ResponseBody
    public ResponseEntity<?> getScheduleList(@RequestParam("yearMonth") String yearMonth, HttpSession session) {
        LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
        if (loginUser == null) return ResponseEntity.status(401).body("로그인이 필요합니다.");

        List<ScheduleDto> list = scheduleService.getMonthlySchedules(loginUser.getEmpNo(), yearMonth);
        return ResponseEntity.ok(list);
    }

    // 일정 등록 REST API
    @PostMapping("/api/save")
    @ResponseBody
    public ResponseEntity<String> saveSchedule(@RequestBody ScheduleDto dto, HttpSession session) {
        LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
        if (loginUser == null) return ResponseEntity.status(401).body("로그인이 필요합니다.");

        try {
            scheduleService.saveSchedule(loginUser.getEmpNo(), dto);
            return ResponseEntity.ok("일정이 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 일정 삭제 REST API
    @DeleteMapping("/api/delete/{schId}")
    @ResponseBody
    public ResponseEntity<String> deleteSchedule(@PathVariable("schId") Integer schId, HttpSession session) {
        LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
        if (loginUser == null) return ResponseEntity.status(401).body("로그인이 필요합니다.");

        try {
            scheduleService.deleteSchedule(schId, loginUser.getEmpNo());
            return ResponseEntity.ok("일정이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}