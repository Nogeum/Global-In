package com.global.login.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "leave_balance")
@Getter @Setter
@NoArgsConstructor
@IdClass(LeaveBalanceId.class)
public class LeaveBalance {

    @Id
    @Column(name = "BAL_EMP_NO", length = 20)
    private String balEmpNo;

    @Id
    @Column(name = "YEAR", length = 4)
    private String year;

    @Column(name = "TOTAL_DAYS")
    private Integer totalDays;

    @Column(name = "USED_DAYS")
    private Integer usedDays;

    @Column(name = "REMAIN_DAYS")
    private Integer remainDays;
}