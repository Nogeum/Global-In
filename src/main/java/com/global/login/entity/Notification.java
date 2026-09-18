package com.global.login.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "notification")
@Getter @Setter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTI_ID")
    private Integer notiId;

    @Column(name = "RECEIVER_NO", length = 20)
    private String receiverNo;

    @Column(name = "CONTENT", length = 500)
    private String content;

    @Column(name = "NOTI_TYPE", length = 20)
    private String notiType;

    @Column(name = "SENT_DATE")
    private LocalDate sentDate;

    @Column(name = "READ_YN", length = 1)
    private String readYn;
}