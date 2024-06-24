package com.example.app.domain.service.message;

import com.example.app.domain.dto.Message.MessageRequestDto;
import com.example.app.domain.dto.Message.MessageResponseDto;
import com.example.app.domain.dto.Message.MessageRoomDto;
import com.example.app.domain.entity.Message.Message;
import com.example.app.domain.entity.Message.MessageRoom;
import com.example.app.domain.entity.Message.Post;
import com.example.app.domain.entity.User;
import com.example.app.domain.repository.Message.MessageRepository;
import com.example.app.domain.repository.Message.MessageRoomRepository;
import com.example.app.domain.repository.Message.PostRepository;
import com.example.app.domain.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageRoomService {
    private final MessageRoomRepository messageRoomRepository;
    private final MessageRepository messageRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 쪽지방(topic)에 발행되는 메시지 처리하는 리스너
    private final RedisMessageListenerContainer redisMessageListener;

    // 구독 처리 서비스
    private final RedisSubscriber redisSubscriber;

    // 1. redis
    private static final String Message_Rooms = "MESSAGE_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, MessageRoomDto> opsHashMessageRoom;

    // 2. 쪽지방의 대화 메시지 발행을 위한 redis topic(쪽지방) 정보
    private Map<String, ChannelTopic> topics;

    // 3. redis 의 Hash 데이터 다루기 위함
    @PostConstruct
    private void init() {
        opsHashMessageRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    // 쪽지방 생성
    public MessageResponseDto createRoom(MessageRequestDto messageRequestDto, User user) {
        Post post = postRepository.findById(messageRequestDto.getPostId()).orElseThrow(
                () -> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
        );

        // 4.
        MessageRoom messageRoom = messageRoomRepository.findBySenderAndReceiver(user.getUserName(), messageRequestDto.getReceiver());

        // 5. 처음 쪽지방 생성 또는 이미 생성된 쪽지방이 아닌 경우
        if ((messageRoom == null) || (messageRoom != null && (!user.getUserName().equals(messageRoom.getSender()) && !messageRequestDto.getReceiver().equals(messageRoom.getReceiver()) && !messageRequestDto.getPostId().equals(post.getPostId())))) {
            MessageRoomDto messageRoomDto = MessageRoomDto.create(messageRequestDto, user);
            opsHashMessageRoom.put(Message_Rooms, messageRoomDto.getRoomId(), messageRoomDto);      // redis hash 에 쪽지방 저장해서, 서버간 채팅방 공유
            messageRoom = messageRoomRepository.save(new MessageRoom(messageRoomDto.getId(), messageRoomDto.getRoomName(), messageRoomDto.getSender(), messageRoomDto.getRoomId(), messageRoomDto.getReceiver(), user, post));

            return new MessageResponseDto(messageRoom);
            // 6. 이미 생성된 쪽지방인 경우
        } else {
            return new MessageResponseDto(messageRoom.getRoomId());
        }
    }

    // 7. 사용자 관련 쪽지방 전체 조회
    public List<MessageResponseDto> findAllRoomByUser(User user) {
        List<MessageRoom> messageRooms = messageRoomRepository.findByUserOrReceiver(user, user.getUserName());      // sender & receiver 모두 해당 쪽지방 조회 가능 (1:1 대화)

        List<MessageResponseDto> messageRoomDtos = new ArrayList<>();

        for (MessageRoom messageRoom : messageRooms) {
            //  user 가 sender 인 경우
            if (user.getUserName().equals(messageRoom.getSender())) {
                MessageResponseDto messageRoomDto = new MessageResponseDto(
                        messageRoom.getId(),
                        messageRoom.getReceiver(),        // roomName
                        messageRoom.getRoomId(),
                        messageRoom.getSender(),
                        messageRoom.getReceiver());

                // 8. 가장 최신 메시지 & 생성 시간 조회
                Message latestMessage = messageRepository.findTopByRoomIdOrderByCreatedAtDesc(messageRoom.getRoomId());
                if (latestMessage != null) {
                    messageRoomDto.setLatestMessageCreatedAt(latestMessage.getCreatedAt());
                    messageRoomDto.setLatestMessageContent(latestMessage.getMessage());
                }

                messageRoomDtos.add(messageRoomDto);
                // user 가 receiver 인 경우
            } else {
                MessageResponseDto messageRoomDto = new MessageResponseDto(
                        messageRoom.getId(),
                        messageRoom.getSender(),        // roomName
                        messageRoom.getRoomId(),
                        messageRoom.getSender(),
                        messageRoom.getReceiver());

                // 가장 최신 메시지 & 생성 시간 조회
                Message latestMessage = messageRepository.findTopByRoomIdOrderByCreatedAtDesc(messageRoom.getRoomId());
                if (latestMessage != null) {
                    messageRoomDto.setLatestMessageCreatedAt(latestMessage.getCreatedAt());
                    messageRoomDto.setLatestMessageContent(latestMessage.getMessage());
                }

                messageRoomDtos.add(messageRoomDto);
            }
        }

        return messageRoomDtos;
    }

    // 사용자 관련 쪽지방 선택 조회
    public MessageRoomDto findRoom(String roomId, User user) {
        MessageRoom messageRoom = messageRoomRepository.findByRoomId(roomId);

        // 게시글 조회
        Post post = postRepository.findById(messageRoom.getPost().getPostId()).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        // 사용자 조회
        User receiver = userRepository.findById(post.getUser().getUserId()).orElseThrow(
                () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
        );

        // 9. sender & receiver 모두 messageRoom 조회 가능
        messageRoom = messageRoomRepository.findByRoomIdAndUserOrRoomIdAndReceiver(roomId, user, roomId, receiver.getUserName());
        if (messageRoom == null) {
            throw new IllegalArgumentException("쪽지방이 존재하지 않습니다.");
        }

        MessageRoomDto messageRoomDto = new MessageRoomDto(
                messageRoom.getId(),
                messageRoom.getRoomName(),
                messageRoom.getRoomId(),
                messageRoom.getSender(),
                messageRoom.getReceiver());

        messageRoomDto.setMessageRoomPostId(post.getPostId());
        return messageRoomDto;
    }

    // 10. 쪽지방 삭제
    public MessageResponseDto deleteRoom(Long id, User user) {
        MessageRoom messageRoom = messageRoomRepository.findByIdAndUserOrIdAndReceiver(id, user, id, user.getUserName());

        // sender 가 삭제할 경우
        if (user.getUserName().equals(messageRoom.getSender())) {
            messageRoomRepository.delete(messageRoom);
            opsHashMessageRoom.delete(Message_Rooms, messageRoom.getRoomId());
            // receiver 가 삭제할 경우
        } else if (user.getUserName().equals(messageRoom.getReceiver())) {
            messageRoom.setReceiver("Not_Exist_Receiver");
            messageRoomRepository.save(messageRoom);
        }

        return new MessageResponseDto(("쪽지방을 삭제했습니다."), HttpStatus.OK.value());
    }

    // 쪽지방 입장
    public void enterMessageRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);

        if (topic == null) {
            topic = new ChannelTopic(roomId);
            redisMessageListener.addMessageListener(redisSubscriber, topic);        // pub/sub 통신을 위해 리스너를 설정. 대화가 가능해진다
            topics.put(roomId, topic);
        }
    }

    // redis 채널에서 쪽지방 조회
    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }
}
