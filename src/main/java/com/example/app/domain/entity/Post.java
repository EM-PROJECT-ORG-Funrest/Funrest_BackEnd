package com.example.app.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "message")
@NoArgsConstructor
public class Post {
    @Id
    private Long postId;

    @ManyToMany
    @JoinColumn(name = "userId")
    private User user;

}
