package com.example.app.domain.service.message;

import com.example.app.domain.dto.Message.MessageDto;
import com.example.app.domain.entity.Message.Message;
import com.example.app.domain.repository.Message.MessageRepository;
import com.example.app.domain.repository.Message.MessageRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final RedisTemplate<String, MessageDto> redisTemplateMessage;
    private final MessageRepository messageRepository;
    private final MessageRoomRepository messageRoomRepository;

    // 대화 저장
    public void saveMessage(MessageDto messageDto) {
        // DB 저장
        Message message = new Message(messageDto.getSender(), messageDto.getRoomId(), messageDto.getMessage());
        messageRepository.save(message);

        // 1. 직렬화
        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(Message.class));

        // 2. redis 저장
        redisTemplateMessage.opsForList().rightPush(messageDto.getRoomId(), messageDto);

        // 3. expire 을 이용해서, Key 를 만료시킬 수 있음
        redisTemplateMessage.expire(messageDto.getRoomId(), 1, TimeUnit.MINUTES);
    }

    // 6. 대화 조회 - Redis & DB
    public List<MessageDto> loadMessage(String roomId) {
        List<MessageDto> messageList = new ArrayList<>();

        // Redis 에서 해당 채팅방의 메시지 100개 가져오기
        List<MessageDto> redisMessageList = redisTemplateMessage.opsForList().range(roomId, 0, 99);

        // 4. Redis 에서 가져온 메시지가 없다면, DB 에서 메시지 100개 가져오기
        if (redisMessageList == null || redisMessageList.isEmpty()) {
            // 5.
            List<Message> dbMessageList = messageRepository.findTop100ByRoomIdOrderByCreatedAtAsc(roomId);

            for (Message message : dbMessageList) {
                MessageDto messageDto = new MessageDto(message);
                messageList.add(messageDto);
                redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(Message.class));      // 직렬화
                redisTemplateMessage.opsForList().rightPush(roomId, messageDto);                                // redis 저장
            }
        } else {
            // 7.
            messageList.addAll(redisMessageList);
        }

        return messageList;
    }
}
