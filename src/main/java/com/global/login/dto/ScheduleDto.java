package com.global.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDto {
    private Integer schId;
    private String workDate; // yyyy-MM-dd
    private String category; // 개인, 팀 회의 등
    private String memo;
    private String color;
}