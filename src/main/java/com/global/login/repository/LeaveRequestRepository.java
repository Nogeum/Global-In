package com.global.login.repository;

import com.global.login.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {

    // 특정 사원의 휴가 신청 목록 조회 (최신순)
    List<LeaveRequest> findByReqEmpNoOrderByReqIdDesc(String reqEmpNo);

    // 특정 사원의 특정 상태(예: 'PENDING' 대기 중) 휴가 신청 건수 집계 (에러 해결 추가 메서드)
    long countByReqEmpNoAndReqStatus(String reqEmpNo, String reqStatus);
}