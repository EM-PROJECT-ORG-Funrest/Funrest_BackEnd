package com.example.app.domain.entity.Message;

import com.example.app.domain.entity.User;
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

    @OneToOne
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "FK_POST_USER",
            foreignKeyDefinition = "FOREIGN KEY(userId) REFERENCES tbl_user(userId) ON DELETE CASCADE ON UPDATE CASCADE"))
    private User user;

}
