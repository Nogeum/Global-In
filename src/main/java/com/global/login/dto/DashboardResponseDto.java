package com.global.login.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DashboardResponseDto {
    private String empName;         // 사원명
    private String checkInTime;     // 출근시간 (hh:mm 또는 "--:--")
    private String checkOutTime;    // 퇴근시간 (hh:mm 또는 "--:--")
    private boolean isCheckedIn;    // 출근 완료 여부
    private boolean isCheckedOut;   // 퇴근 완료 여부
    private Integer remainLeaveDays;// 잔여 연차
    private int pendingApprovalCount; // 결제 대기건수
    private List<NoticeDto> notices; // 사내 공지사항

    @Getter
    @Builder
    public static class NoticeDto {
        private String content;
        private String date;
    }
}