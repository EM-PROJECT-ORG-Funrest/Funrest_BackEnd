package com.example.app.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_session")
public class Session {
    @Id
    @Column(nullable = false)
    private String sessionId;
    @OneToOne
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "FK_SESSION_USER",
            foreignKeyDefinition = "FOREIGN KEY(userId) REFERENCES tbl_user(userId) ON DELETE CASCADE ON UPDATE CASCADE"))
    private User userId;
}
