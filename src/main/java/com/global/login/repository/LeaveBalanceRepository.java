package com.global.login.repository;

import com.global.login.entity.LeaveBalance;
import com.global.login.entity.LeaveBalanceId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, LeaveBalanceId> {
}