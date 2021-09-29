package com.chatground.repository;

import com.chatground.entity.GroundMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface GroundMessageRepository extends JpaRepository<GroundMessage, Long> {
    List<GroundMessage> findByTimeBetween(Timestamp start, Timestamp end);
}
