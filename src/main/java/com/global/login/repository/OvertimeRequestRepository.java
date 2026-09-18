package com.global.login.repository;

import com.global.login.entity.OvertimeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OvertimeRequestRepository extends JpaRepository<OvertimeRequest, Long> {
    List<OvertimeRequest> findByEmpNoOrderByOtReqIdDesc(String empNo);
}