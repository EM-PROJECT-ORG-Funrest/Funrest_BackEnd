package com.example.app.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_notify")
public class Notify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notifyCode", nullable = false)
    private int notifyCode;
    @ManyToOne
    @JoinColumn(name = "proCode", foreignKey = @ForeignKey(name = "FK_NOTIFY_PROJECT",
    foreignKeyDefinition = "FOREIGN KEY(proCode) REFERENCES tbl_project(proCode) ON DELETE CASCADE ON UPDATE CASCADE"))
    private Project proCode;
    @ManyToOne
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "FK_NOTIFY_USER",
    foreignKeyDefinition = "FOREIGN KEY(userId) REFERENCES tbl_user(userId) ON DELETE CASCADE ON UPDATE CASCADE"))
    private User userId;
    @Column(name = "notifyDate", nullable = false)
    private Date notifyDate;
}
