package com.global.login.service;

import com.global.login.dto.ApprovalDto;
import com.global.login.entity.LeaveBalance;
import com.global.login.entity.LeaveBalanceId;
import com.global.login.entity.LeaveRequest;
import com.global.login.entity.OvertimeRequest;
import com.global.login.entity.PdsBoard;
import com.global.login.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final OvertimeRequestRepository overtimeRequestRepository;
    private final PdsBoardRepository pdsBoardRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final AttendanceRepository attendanceRepository;

    // 1. 휴가 신청 제출
    @Transactional
    public void submitLeaveRequest(String empNo, ApprovalDto.LeaveForm form) {
        LeaveRequest req = LeaveRequest.builder()
                .reqEmpNo(empNo)
                .leaveType(form.getLeaveType())
                .processedBy(form.getApproverEmpNo())
                .startDate(form.getStartDate())
                .endDate(form.getEndDate())
                .reason(form.getReason())
                .reqStatus("PENDING")
                .reqDate(LocalDate.now())
                .build();

        leaveRequestRepository.save(req);
    }

    // 2. 시간외수당 신청 제출
    @Transactional
    public void submitOvertimeRequest(String empNo, ApprovalDto.OvertimeForm form) {
        OvertimeRequest req = OvertimeRequest.builder()
                .empNo(empNo)
                .approverEmpNo(form.getApproverEmpNo())
                .workDate(form.getWorkDate())
                .startTime(form.getStartTime())
                .endTime(form.getEndTime())
                .reason(form.getReason())
                .reqStatus("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        overtimeRequestRepository.save(req);
    }

    // 3. 잔여 연차 조회
    @Transactional(readOnly = true)
    public double getRemainLeave(String empNo) {
        int currentYear = LocalDate.now().getYear();
        return leaveBalanceRepository.findById(new LeaveBalanceId(empNo, String.valueOf(currentYear)))
                .map(LeaveBalance::getRemainDays)
                .orElse(0)
                .doubleValue();
    }

    // 4. 당월 누적 잔업시간 (시간 단위 소수점 계산, 예: 8.5시간)
    @Transactional(readOnly = true)
    public double getMonthlyOvertimeHours(String empNo) {
        YearMonth ym = YearMonth.now();
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        Long totalOtMinutes = attendanceRepository.sumOtMinutesByEmpNoAndMonth(empNo, start, end);
        if (totalOtMinutes == null) return 0.0;

        return Math.round((totalOtMinutes / 60.0) * 10.0) / 10.0;
    }

    // 5. 자료실 목록 조회 (검색어 대응)
    @Transactional(readOnly = true)
    public List<PdsBoard> getPdsList(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return pdsBoardRepository.findByTitleContainingIgnoreCaseOrderByFileIdDesc(keyword.trim());
        }
        return pdsBoardRepository.findAllByOrderByFileIdDesc();
    }
}