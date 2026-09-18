package com.global.login.repository;

import com.global.login.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    // 직원의 알림 목록 (최신순 5개)
    List<Notification> findTop5ByReceiverNoOrderBySentDateDesc(String receiverNo);
}