package com.example.app.domain.dto;

import com.example.app.domain.entity.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageDto {
    private String sender;
    private String roomId;
    private String message;
    private String sentTime;

    // 대화 조회
    public MessageDto(Message message) {
        this.sender = message.getSender();
        this.roomId = message.getRoomId();
        this.message = message.getMessage();
    }
}