package com.global.login.repository;

import com.global.login.entity.PdsBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PdsBoardRepository extends JpaRepository<PdsBoard, Long> {
    List<PdsBoard> findByTitleContainingIgnoreCaseOrderByFileIdDesc(String keyword);
    List<PdsBoard> findAllByOrderByFileIdDesc();
}