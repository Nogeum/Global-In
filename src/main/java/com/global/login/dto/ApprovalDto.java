package com.global.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

public class ApprovalDto {

    // 휴가 신청 Form DTO
    @Data
    public static class LeaveForm {
        private String leaveType;     // 휴가, 반차 등
        private String approverEmpNo; // 결재자 사원번호
        private LocalDate startDate;
        private LocalDate endDate;
        private String reason;
    }

    // 시간외수당 신청 Form DTO
    @Data
    public static class OvertimeForm {
        private String approverEmpNo; // 결재자 사원번호
        private LocalDate workDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private String reason;
    }

    // 이번달 팀 휴가자 정보 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TeamLeaveDto {
        private String empName;
        private String leaveDateStr; // 예: "9/28"
    }
}