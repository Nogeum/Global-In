package com.global.login.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
@Getter @Setter
@NoArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ATT_ID")
    private Integer attId;

    @Column(name = "EMP_NO", nullable = false, length = 20)
    private String empNo;

    @Column(name = "WORK_DATE", nullable = false)
    private LocalDate workDate;

    @Column(name = "CHECK_IN_TIME")
    private LocalDateTime checkInTime;

    @Column(name = "CHECK_OUT_TIME")
    private LocalDateTime checkOutTime;

    @Column(name = "WORK_MINUTES")
    private Integer workMinutes = 0;

    @Column(name = "OT_MINUTES")
    private Integer otMinutes = 0;

    @Column(name = "CHECK_LOCATION", length = 100)
    private String checkLocation;

    @Column(name = "CHECK_METHOD", length = 10)
    private String checkMethod;

    @Column(name = "ATT_STATUS", length = 10)
    private String attStatus; // 'NORMAL', 'LATE', 'ABSENT', 'LEAVE'
}