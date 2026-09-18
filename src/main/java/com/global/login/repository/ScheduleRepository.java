package com.global.login.repository;

import com.global.login.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    // 특정 사원의 특정 기간(해당 월) 일정 조회
    List<Schedule> findBySchEmpNoAndWorkDateBetween(String schEmpNo, LocalDate startDate, LocalDate endDate);
}