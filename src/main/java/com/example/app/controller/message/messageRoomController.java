package com.example.app.controller.message;

import com.example.app.domain.dto.Message.MessageRequestDto;
import com.example.app.domain.dto.Message.MessageResponseDto;
import com.example.app.domain.dto.Message.MessageRoomDto;
import com.example.app.domain.entity.User;
import com.example.app.domain.repository.UserRepository;
import com.example.app.domain.service.message.MessageRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/th/api")
@RequiredArgsConstructor
@Slf4j
public class messageRoomController {
    private final MessageRoomService messageRoomService;
    private final UserRepository userRepository;
    // 쪽지방 생성
    @PostMapping("/room")
    public  MessageResponseDto createRoom(@RequestBody MessageRequestDto messageRequestDto) {
        String userID = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUserId(userID).orElseThrow(()  -> new RuntimeException());
        return messageRoomService.createRoom(messageRequestDto, user);
    }

    @GetMapping("/test")
    public void a (){
        log.info("test invoked....");

    }

    // 사용자 관련 쪽지방 전체 조회
    @GetMapping("/rooms")
    public List<MessageResponseDto> findAllRoomByUser() {
        String userID = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUserId(userID).orElseThrow(()  -> new RuntimeException());
        List<MessageResponseDto> MessageResponseDto =  messageRoomService.findAllRoomByUser(user);

        log.info("user : " + user);
        log.info("MessageResponseDto" + MessageResponseDto);

        return MessageResponseDto;
    }

    // 사용자 관련 쪽지방 선택 조회
    @GetMapping("room/{roomId}")
    public MessageRoomDto findRoom(@PathVariable String roomId) {
        String userID = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUserId(userID).orElseThrow(()  -> new RuntimeException());
        return messageRoomService.findRoom(roomId, user);
    }

    // 쪽지방 삭제
    @DeleteMapping("room/{id}")
    MessageResponseDto deleteRoom(@PathVariable Long id) {
        String userID = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUserId(userID).orElseThrow(()  -> new RuntimeException());
        return messageRoomService.deleteRoom(id, user);
    }
}
