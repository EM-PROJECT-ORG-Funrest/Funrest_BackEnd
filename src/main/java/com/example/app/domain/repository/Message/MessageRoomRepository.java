package com.example.app.domain.repository.Message;

import com.example.app.domain.entity.Message.MessageRoom;
import com.example.app.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRoomRepository extends JpaRepository<MessageRoom, Long> {

    List<MessageRoom> findByUserOrReceiver(User user, String receiver);

    MessageRoom findByIdAndUserOrIdAndReceiver(Long id, User user, Long id1, String nickname);

    MessageRoom findBySenderAndReceiver(String nickname, String receiver);

    MessageRoom findByRoomIdAndUserOrRoomIdAndReceiver(String roomId, User user, String roomId1, String nickname);

    MessageRoom findByRoomId(String roomId);
}