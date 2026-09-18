package com.global.login.service;

import com.global.login.dto.DashboardResponseDto;
import com.global.login.entity.*;
import com.global.login.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AttendanceRepository attendanceRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public DashboardResponseDto getDashboardData(String empNo, String empName) {
        LocalDate today = LocalDate.now();
        String currentYear = String.valueOf(today.getYear());

        // 1. 금일 출퇴근 현황
        Optional<Attendance> attOpt = attendanceRepository.findByEmpNoAndWorkDate(empNo, today);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String checkIn = "--:--";
        String checkOut = "--:--";
        boolean isCheckedIn = false;
        boolean isCheckedOut = false;

        if (attOpt.isPresent()) {
            Attendance att = attOpt.get();
            if (att.getCheckInTime() != null) {
                checkIn = att.getCheckInTime().format(timeFormatter);
                isCheckedIn = true;
            }
            if (att.getCheckOutTime() != null) {
                checkOut = att.getCheckOutTime().format(timeFormatter);
                isCheckedOut = true;
            }
        }

        // 2. 잔여 연차 조회 (데이터가 없는 경우 0일 기본값)
        LeaveBalanceId balanceId = new LeaveBalanceId(empNo, currentYear);
        Integer remainDays = leaveBalanceRepository.findById(balanceId)
                .map(LeaveBalance::getRemainDays)
                .orElse(0);

        // 3. 결제 대기건수 (REQ_STATUS가 PENDING인 건수)
        int pendingCount = (int)leaveRequestRepository.countByReqEmpNoAndReqStatus(empNo, "PENDING");

        // 4. 사내 공지사항 (최신 5건)
        DateTimeFormatter dateDotFormatter = DateTimeFormatter.ofPattern("MM/dd");
        List<DashboardResponseDto.NoticeDto> notices = notificationRepository.findTop5ByReceiverNoOrderBySentDateDesc(empNo)
                .stream()
                .map(n -> DashboardResponseDto.NoticeDto.builder()
                        .content(n.getContent())
                        .date(n.getSentDate() != null ? n.getSentDate().format(dateDotFormatter) : "")
                        .build())
                .collect(Collectors.toList());

        return DashboardResponseDto.builder()
                .empName(empName)
                .checkInTime(checkIn)
                .checkOutTime(checkOut)
                .isCheckedIn(isCheckedIn)
                .isCheckedOut(isCheckedOut)
                .remainLeaveDays(remainDays)
                .pendingApprovalCount(pendingCount)
                .notices(notices)
                .build();
    }

    // 출근 등록
    @Transactional
    public void checkIn(String empNo) {
        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository.findByEmpNoAndWorkDate(empNo, today)
                .orElseGet(() -> {
                    Attendance att = new Attendance();
                    att.setEmpNo(empNo);
                    att.setWorkDate(today);
                    att.setAttStatus("NORMAL");
                    return att;
                });

        if (attendance.getCheckInTime() == null) {
            attendance.setCheckInTime(LocalDateTime.now());
            attendanceRepository.save(attendance);
        }
    }

    // 퇴근 등록
    @Transactional
    public void checkOut(String empNo) {
        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository.findByEmpNoAndWorkDate(empNo, today)
                .orElseThrow(() -> new IllegalStateException("출근 기록이 존재하지 않습니다. 먼저 출근을 등록해주세요."));

        if (attendance.getCheckOutTime() == null) {
            attendance.setCheckOutTime(LocalDateTime.now());
            attendanceRepository.save(attendance);
        }
    }
}