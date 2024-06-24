package com.example.app.domain.dto.Message;

import com.example.app.domain.entity.Message.MessageRoom;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponseDto {
    private Long id;
    private String roomName;
    private String sender;
    private String roomId;
    private String receiver;
    private Long postId;
    private String message;
    private LocalDateTime createdAt;

    // 쪽지방 생성
    public MessageResponseDto(MessageRoom messageRoom) {
        this.id = messageRoom.getId();
        this.roomName = messageRoom.getRoomName();
        this.sender = messageRoom.getSender();
        this.roomId = messageRoom.getRoomId();
        this.receiver = messageRoom.getReceiver();
        this.postId = messageRoom.getPost().getPostId();
    }

    // 사용자 관련 쪽지방 전체 조회
    public MessageResponseDto(Long id, String roomName, String roomId, String sender, String receiver) {
        this.id = id;
        this.roomName = roomName;
        this.roomId = roomId;
        this.sender = sender;
        this.receiver = receiver;
    }

    public MessageResponseDto(String roomId) {
        this.roomId = roomId;
    }

    public MessageResponseDto(String s, int value) {
    }

    public void setLatestMessageContent(String message) {
        this.message = message;
    }

    public void setLatestMessageCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}