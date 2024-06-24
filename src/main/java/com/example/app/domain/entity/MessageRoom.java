package com.example.app.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "messageRoom")
@NoArgsConstructor
public class MessageRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;

    private String sender;			// 채팅방 생성자

    @Column(unique = true)
    private String roomId;

    private String receiver;        // 채팅방 수신자

    @OneToMany(mappedBy = "messageRoom", cascade = CascadeType.REMOVE)
    private List<Message> messageList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @OneToOne			// 채팅방과 Post 는 1:1 연관관계
    @JoinColumn(name = "postId")
    private Post post;

    // 쪽지방 생성
    public MessageRoom(Long id, String roomName, String sender, String roomId, String receiver, User user, Post post) {
        this.id = id;
        this.roomName = roomName;
        this.sender = sender;
        this.roomId = roomId;
        this.receiver = receiver;
        this.user = user;
        this.post = post;
    }
}