package com.global.login.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "leave_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQ_ID")
    private Integer reqId;

    @Column(name = "REQ_EMP_NO", nullable = false, length = 20)
    private String reqEmpNo;

    @Column(name = "LEAVE_TYPE", nullable = false, length = 10)
    private String leaveType;

    @Column(name = "START_DATE", nullable = false)
    private LocalDate startDate;

    @Column(name = "END_DATE", nullable = false)
    private LocalDate endDate;

    @Column(name = "REASON", length = 500)
    private String reason;

    @Column(name = "REQ_STATUS", length = 10)
    private String reqStatus; // PENDING, APPROVED, REJECTED

    @Column(name = "REQ_DATE")
    private LocalDate reqDate;

    @Column(name = "PROCESS_DATE")
    private LocalDate processDate;

    @Column(name = "PROCESS_REASON", length = 500)
    private String processReason;

    @Column(name = "PROCESSED_BY", length = 20)
    private String processedBy; // 결재자 사원번호 (APPROVER)
}