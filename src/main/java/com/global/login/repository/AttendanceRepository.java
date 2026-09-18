package com.global.login.repository;

import com.global.login.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    // 특정 사원의 당일 근태 조회
    Optional<Attendance> findByEmpNoAndWorkDate(String empNo, LocalDate workDate);

    // 기간 내 근태 목록 조회
    List<Attendance> findByEmpNoAndWorkDateBetween(String empNo, LocalDate startDate, LocalDate endDate);

    // 당월 총 근무분(WORK_MINUTES) 합계
    @Query("SELECT SUM(a.workMinutes) FROM Attendance a WHERE a.empNo = :empNo AND a.workDate BETWEEN :startDate AND :endDate")
    Long sumWorkMinutesByEmpNoAndMonth(@Param("empNo") String empNo, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 당월 총 초과근무분(OT_MINUTES) 합계
    @Query("SELECT SUM(a.otMinutes) FROM Attendance a WHERE a.empNo = :empNo AND a.workDate BETWEEN :startDate AND :endDate")
    Long sumOtMinutesByEmpNoAndMonth(@Param("empNo") String empNo, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 당월 특정 상태(지각 LATE, 결근 ABSENT) 횟수 조회
    int countByEmpNoAndWorkDateBetweenAndAttStatus(String empNo, LocalDate startDate, LocalDate endDate, String attStatus);
}