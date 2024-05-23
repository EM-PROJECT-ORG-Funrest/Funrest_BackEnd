package com.example.app.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proCode", nullable = false)
    private int proCode;
    @ManyToOne
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "FK_PROJECT_USER",
    foreignKeyDefinition = "FOREIGN KEY(userId) REFERENCES tbl_user(userId) ON DELETE CASCADE ON UPDATE CASCADE"))
    private User userId;
    @Column(name = "proCategory", nullable = false)
    private String proCategory;
    @Column(name = "proName", nullable = false)
    private String proName;
    @Column(name = "proImg", nullable = false)
    private String proImg; // project's all images
    @Column(name = "proMainImg", nullable = false)
    private boolean proMainImg; // project's main image
    @Column(name = "proPrice", nullable = false)
    private int proPrice;
    @Column(name = "proDate", nullable = false)
    private Date proDate; // project's permission date
    @Column(name = "proStartDate", nullable = false)
    private Date proStartDate;
    @Column(name = "proEndDate", nullable = false)
    private Date proEndDate;
    @Column(name = "proStatus", nullable = false)
    private boolean proStatus; // project permission (Y/N)
    @Column(name = "proPaidCnt", nullable = false, columnDefinition = "integer default 0")
    private int proPaidCnt;
    @Column(name = "proNotifyCnt", nullable = false, columnDefinition = "integer default 0")
    private int proNotifyCnt;
    @Column(name = "proScript", nullable = false)
    private String proScript;
    @Column(name = "sellerName", nullable = false)
    private String sellerName;
    @Column(name = "sellerDetail", nullable = false)
    private String sellerDetail;
}
