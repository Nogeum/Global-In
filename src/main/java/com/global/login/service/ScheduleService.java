package com.global.login.service;

import com.global.login.dto.ScheduleDto;
import com.global.login.entity.Schedule;
import com.global.login.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    // 해당 월의 일정 조회 API
    @Transactional(readOnly = true)
    public List<ScheduleDto> getMonthlySchedules(String empNo, String yearMonth) {
        YearMonth ym = YearMonth.parse(yearMonth); // e.g. "2026-09"
        LocalDate startDate = ym.atDay(1);
        LocalDate endDate = ym.atEndOfMonth();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return scheduleRepository.findBySchEmpNoAndWorkDateBetween(empNo, startDate, endDate)
                .stream()
                .map(s -> ScheduleDto.builder()
                        .schId(s.getSchId())
                        .workDate(s.getWorkDate().format(formatter))
                        .category(s.getCategory())
                        .memo(s.getMemo())
                        .color(s.getColor())
                        .build())
                .collect(Collectors.toList());
    }

    // 일정 등록
    @Transactional
    public void saveSchedule(String empNo, ScheduleDto dto) {
        Schedule schedule = new Schedule();
        schedule.setSchEmpNo(empNo);
        schedule.setWorkDate(LocalDate.parse(dto.getWorkDate()));
        schedule.setCategory(dto.getCategory());
        schedule.setMemo(dto.getMemo());
        schedule.setColor(dto.getColor() != null ? dto.getColor() : "#3f51b5");
        schedule.setRegDate(LocalDate.now());

        scheduleRepository.save(schedule);
    }

    // 일정 삭제
    @Transactional
    public void deleteSchedule(Integer schId, String empNo) {
        Schedule schedule = scheduleRepository.findById(schId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다."));

        if (!schedule.getSchEmpNo().equals(empNo)) {
            throw new IllegalStateException("본인의 일정만 삭제할 수 있습니다.");
        }

        scheduleRepository.delete(schedule);
    }
}