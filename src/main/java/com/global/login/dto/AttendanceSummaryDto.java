package com.global.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceSummaryDto {
    private String monthLabel;          // 예: "2026년 9월"
    private String workDays;            // 이번 달 근무일 (예: "11 / 12일")
    private String totalWorkTime;       // 총 근무시간 (예: "92:40")
    private String totalOvertime;       // 누적 초과근무 (예: "6:40")
    private String tardyAndAbsent;      // 지각 · 결근 (예: "1 · 1")
    private double remainLeave;         // 잔여 연차 (예: 11.5)

    private String weekLabel;           // 주차 표기 (예: "9월 3주")
    private List<DailyAttendanceDto> weeklyAttendance; // 7일간 출퇴근 이력
    private List<LeaveRequestDto> leaveRequests;       // 휴가 신청 내역

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DailyAttendanceDto {
        private String dateStr;         // yyyy-MM-dd
        private int dayNum;             // 일자 (13, 14, 15...)
        private String timeRange;       // "20:42 ~ 20:42" 또는 "기록 없음"
        private String badgeText;       // "정상출근", "지각", "결근", "휴가"
        private String badgeColor;      // "#2ecc71", "#f39c12", "#e74c3c", "#3498db"
        private boolean isToday;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LeaveRequestDto {
        private Long reqId;
        private String leaveType;       // 연차, 반차 등
        private String startDate;
        private String endDate;
        private String status;          // 승인, 대기, 반려
    }
}