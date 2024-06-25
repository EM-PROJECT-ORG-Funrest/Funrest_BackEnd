package com.example.app.domain.dto.Message;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class MessageRequestDto {
    private String receiver;    // 메세지 수신자
    private Long postId;        // 게시글 id
}