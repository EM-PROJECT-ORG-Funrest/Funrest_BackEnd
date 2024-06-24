package com.example.app.domain.repository.Message;

import com.example.app.domain.entity.Message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findTop100ByRoomIdOrderByCreatedAtAsc(String roomId);

    Message findTopByRoomIdOrderByCreatedAtDesc(String roomId);
}