package com.global.login.controller;

import com.global.login.dto.ApprovalDto;
import com.global.login.entity.PdsBoard;
import com.global.login.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/employee/approval")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    // 1. 휴가 신청 페이지 (/employee/approval/leave)
    @GetMapping("/leave")
    public String leavePage(HttpSession session, Model model) {
        String empNo = (String) session.getAttribute("empNo");
        if (empNo == null) empNo = "EMP002"; // 테스트 사원번호

        double remainLeave = approvalService.getRemainLeave(empNo);
        model.addAttribute("remainLeave", remainLeave);

        // ⭕ templates/employee/approval/leave_request.html 을 찾도록 수정
        return "employee/approval/leave_request";
    }

    // 2. 시간외수당 신청 페이지 (/employee/approval/overtime)
    @GetMapping("/overtime")
    public String overtimePage(HttpSession session, Model model) {
        String empNo = (String) session.getAttribute("empNo");
        if (empNo == null) empNo = "EMP002";

        double monthlyOvertimeHours = approvalService.getMonthlyOvertimeHours(empNo);
        model.addAttribute("overtimeHours", monthlyOvertimeHours);

        // ⭕ templates/employee/approval/overtime_request.html 을 찾도록 수정
        return "employee/approval/overtime_request";
    }

    // 3. 자료실 페이지 (/employee/approval/pds)
    @GetMapping("/pds")
    public String pdsPage(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<PdsBoard> pdsList = approvalService.getPdsList(keyword);
        model.addAttribute("pdsList", pdsList);
        model.addAttribute("keyword", keyword);

        // ⭕ templates/employee/approval/pds_list.html 을 찾도록 수정
        return "employee/approval/pds_list";
    }


    // 1. 휴가 신청서 제출 처리 (POST)
    @PostMapping("/leave")
    public String submitLeave(@ModelAttribute ApprovalDto.LeaveForm form, HttpSession session) {
        String empNo = (String) session.getAttribute("empNo");
        if (empNo == null) empNo = "EMP002"; // 테스트용 기본 사원번호

        approvalService.submitLeaveRequest(empNo, form);

        // 제출 완료 후 전자결재 목록 또는 휴가신청 페이지로 리다이렉트
        return "redirect:/employee/approval/leave";
    }

    // 2. 시간외수당 신청서 제출 처리 (POST)
    @PostMapping("/overtime")
    public String submitOvertime(@ModelAttribute ApprovalDto.OvertimeForm form, HttpSession session) {
        String empNo = (String) session.getAttribute("empNo");
        if (empNo == null) empNo = "EMP002"; // 테스트용 기본 사원번호

        approvalService.submitOvertimeRequest(empNo, form);

        // 제출 완료 후 시간외수당 신청 페이지로 리다이렉트
        return "redirect:/employee/approval/overtime";
    }
}