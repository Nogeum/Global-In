package com.global.login.service;

import com.global.login.dto.AttendanceSummaryDto;
import com.global.login.entity.Attendance;
import com.global.login.entity.LeaveBalance;
import com.global.login.entity.LeaveBalanceId;
import com.global.login.repository.AttendanceRepository;
import com.global.login.repository.LeaveBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;

    // 1. 출근 처리 (09:00:00 기준 지각 여부 판정)
    @Transactional
    public void checkIn(String empNo, String location, String method) {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        if (attendanceRepository.findByEmpNoAndWorkDate(empNo, today).isPresent()) {
            throw new IllegalStateException("이미 오늘 출근 등록이 완료되었습니다.");
        }

        Attendance att = new Attendance();
        att.setEmpNo(empNo);
        att.setWorkDate(today);
        att.setCheckInTime(now);
        att.setCheckLocation(location);
        att.setCheckMethod(method);

        // 09:00:00 이전이면 NORMAL, 이후면 LATE
        LocalTime lateStandard = LocalTime.of(9, 0, 0);
        if (now.toLocalTime().isAfter(lateStandard)) {
            att.setAttStatus("LATE");
        } else {
            att.setAttStatus("NORMAL");
        }

        attendanceRepository.save(att);
    }

    // 2. 퇴근 처리 (18:00 기준 초과근무 OT_MINUTES 계산)
    @Transactional
    public void checkOut(String empNo) {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        Attendance att = attendanceRepository.findByEmpNoAndWorkDate(empNo, today)
                .orElseThrow(() -> new IllegalStateException("출근 기록이 존재하지 않습니다."));

        if (att.getCheckOutTime() != null) {
            throw new IllegalStateException("이미 퇴근 등록이 완료되었습니다.");
        }

        att.setCheckOutTime(now);

        // 근무분(WORK_MINUTES) 계산
        long workMin = Duration.between(att.getCheckInTime(), now).toMinutes();
        att.setWorkMinutes((int) workMin);

        // 초과근무분(OT_MINUTES) 계산 (18:00 이후 근무시간)
        LocalDateTime otStandardTime = LocalDateTime.of(today, LocalTime.of(18, 0, 0));
        if (now.isAfter(otStandardTime)) {
            long otMin = Duration.between(otStandardTime, now).toMinutes();
            att.setOtMinutes((int) otMin);
        } else {
            att.setOtMinutes(0);
        }

        attendanceRepository.save(att);
    }

    // 3. 근태 페이지 요약 정보 및 주단위 캘린더 데이터 조회
    @Transactional(readOnly = true)
    public AttendanceSummaryDto getAttendanceSummary(String empNo, LocalDate targetDate) {
        LocalDate today = LocalDate.now();
        if (targetDate == null) targetDate = today;

        YearMonth ym = YearMonth.from(targetDate);
        LocalDate startOfMonth = ym.atDay(1);
        LocalDate endOfMonth = ym.atEndOfMonth();

        // 1) 상단 통계 카드 데이터 구하기
        Long totalWorkMinutes = attendanceRepository.sumWorkMinutesByEmpNoAndMonth(empNo, startOfMonth, endOfMonth);
        if (totalWorkMinutes == null) totalWorkMinutes = 0L;
        String totalWorkTimeStr = String.format("%d:%02d", totalWorkMinutes / 60, totalWorkMinutes % 60);

        Long totalOtMinutes = attendanceRepository.sumOtMinutesByEmpNoAndMonth(empNo, startOfMonth, endOfMonth);
        if (totalOtMinutes == null) totalOtMinutes = 0L;
        String totalOvertimeStr = String.format("%d:%02d", totalOtMinutes / 60, totalOtMinutes % 60);

        // 지각 건수 (DB 조회)
        int lateCount = attendanceRepository.countByEmpNoAndWorkDateBetweenAndAttStatus(empNo, startOfMonth, endOfMonth, "LATE");

        // 결근 건수 (지나간 평일 중 출근 기록이 없거나 ABSENT인 경우 집계)
        int absentCount = 0;
        for (LocalDate date = startOfMonth; date.isBefore(today); date = date.plusDays(1)) {
            boolean isWeekday = (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY);
            if (isWeekday) {
                Optional<Attendance> attOpt = attendanceRepository.findByEmpNoAndWorkDate(empNo, date);
                if (!attOpt.isPresent() || "ABSENT".equals(attOpt.get().getAttStatus())) {
                    absentCount++;
                }
            }
        }
        String tardyAndAbsentStr = lateCount + " · " + absentCount;

        // 이번 달 총 평일 수 및 출근일 수
        int actualWorkDays = 0;
        int totalWeekdays = 0;
        for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
            boolean isWeekday = (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY);
            if (isWeekday) {
                totalWeekdays++;
                if (attendanceRepository.findByEmpNoAndWorkDate(empNo, date).isPresent()) {
                    actualWorkDays++;
                }
            }
        }
        String workDaysStr = String.format("%d / %d일", actualWorkDays, totalWeekdays);

        // 잔여 연차 조회
        double remainLeave = leaveBalanceRepository.findById(new LeaveBalanceId(empNo, String.valueOf(targetDate.getYear())))
                .map(LeaveBalance::getRemainDays)
                .orElse(0)
                .doubleValue();

        // 2) 주단위 캘린더 날짜 계산 (일요일 ~ 토요일)
        LocalDate startOfWeek = targetDate.with(DayOfWeek.SUNDAY);
        if (targetDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            startOfWeek = targetDate;
        } else {
            startOfWeek = targetDate.minusDays(targetDate.getDayOfWeek().getValue());
        }
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        int weekOfMonth = targetDate.get(WeekFields.of(Locale.KOREA).weekOfMonth());
        String weekLabel = String.format("%d월 %d주", targetDate.getMonthValue(), weekOfMonth);

        List<Attendance> weeklyAtts = attendanceRepository.findByEmpNoAndWorkDateBetween(empNo, startOfWeek, endOfWeek);
        Map<LocalDate, Attendance> attMap = new HashMap<>();
        for (Attendance a : weeklyAtts) attMap.put(a.getWorkDate(), a);

        List<AttendanceSummaryDto.DailyAttendanceDto> dailyList = new ArrayList<>();
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            Attendance att = attMap.get(date);

            String timeRange = "기록 없음";
            String badgeText = null;
            String badgeColor = null;
            boolean isWeekday = (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY);

            if (att != null) {
                if (att.getCheckInTime() != null && att.getCheckOutTime() != null) {
                    timeRange = att.getCheckInTime().format(timeFmt) + " ~ " + att.getCheckOutTime().format(timeFmt);
                } else if (att.getCheckInTime() != null) {
                    timeRange = att.getCheckInTime().format(timeFmt) + " ~ --:--";
                }

                String status = att.getAttStatus();
                if ("NORMAL".equals(status)) {
                    badgeText = "정상출근";
                    badgeColor = "#2ecc71"; // 초록색
                } else if ("LATE".equals(status)) {
                    badgeText = "지각";
                    badgeColor = "#f39c12"; // 주황색
                } else if ("LEAVE".equals(status)) {
                    badgeText = "휴가";
                    badgeColor = "#3498db"; // 파란색
                } else if ("ABSENT".equals(status)) {
                    badgeText = "결근";
                    badgeColor = "#e74c3c"; // 빨간색
                }
            } else {
                // 출근 기록이 없고 평일이며 지나간 날짜인 경우 -> 결근 처리
                if (isWeekday && date.isBefore(today)) {
                    badgeText = "결근";
                    badgeColor = "#e74c3c"; // 빨간색
                }
            }

            dailyList.add(AttendanceSummaryDto.DailyAttendanceDto.builder()
                    .dateStr(date.toString())
                    .dayNum(date.getDayOfMonth())
                    .timeRange(timeRange)
                    .badgeText(badgeText)
                    .badgeColor(badgeColor)
                    .isToday(date.equals(today))
                    .build());
        }

        return AttendanceSummaryDto.builder()
                .monthLabel(targetDate.getYear() + "년 " + targetDate.getMonthValue() + "월")
                .workDays(workDaysStr)
                .totalWorkTime(totalWorkTimeStr)
                .totalOvertime(totalOvertimeStr)
                .tardyAndAbsent(tardyAndAbsentStr)
                .remainLeave(remainLeave)
                .weekLabel(weekLabel)
                .weeklyAttendance(dailyList)
                .build();
    }
}