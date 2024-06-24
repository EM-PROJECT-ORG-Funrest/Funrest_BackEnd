package com.example.app.controller.message;

import com.example.app.domain.dto.Message.MessageDto;
import com.example.app.domain.service.message.MessageRoomService;
import com.example.app.domain.service.message.MessageService;
import com.example.app.domain.service.message.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class messageController {
    private final RedisPublisher redisPublisher;
    private final MessageRoomService messageRoomService;
    private final MessageService messageService;

    // 대화 & 대화 저장
    @MessageMapping("/message")     // 1.
    public void message(MessageDto messageDto) {
        // 클라이언트의 쪽지방(topic) 입장, 대화를 위해 리스너와 연동
        messageRoomService.enterMessageRoom(messageDto.getRoomId());
        log.info("messageDto : " + messageDto);

        // Websocket 에 발행된 메시지를 redis 로 발행. 해당 쪽지방을 구독한 클라이언트에게 메시지가 실시간 전송됨 (1:N, 1:1 에서 사용 가능)
        redisPublisher.publish(messageRoomService.getTopic(messageDto.getRoomId()), messageDto);

        // DB & Redis 에 대화 저장
        messageService.saveMessage(messageDto);
    }

    // 대화 내역 조회
    @GetMapping("/api/room/{roomId}/message")
    public ResponseEntity<List<MessageDto>> loadMessage(@PathVariable String roomId) {
        return ResponseEntity.ok(messageService.loadMessage(roomId));
    }
}