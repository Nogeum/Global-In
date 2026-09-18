package com.global.login.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "schedule")
@Getter @Setter
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCH_ID")
    private Integer schId;

    @Column(name = "SCH_EMP_NO", nullable = false, length = 20)
    private String schEmpNo;

    @Column(name = "TITLE", length = 100)
    private String title;

    @Column(name = "CATEGORY", length = 20)
    private String category; // '개인', '팀 회의' 등

    @Column(name = "MEMO", length = 500)
    private String memo;

    @Column(name = "COLOR", length = 20)
    private String color; // '#e74c3c', '#3f51b5' 등

    @Column(name = "WORK_DATE", nullable = false)
    private LocalDate workDate;

    @Column(name = "REG_DATE")
    private LocalDate regDate = LocalDate.now();
}