package com.example.app.domain.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDto {
    private int proCode;
    private String userId;
    private String proCategory;
    private String proName;
    private String proImg; // 프로젝트 모든 이미지
    private boolean proMainImg; // 프로젝트 썸네일
    private int proPrice;
    private Date proDate; // 프로젝트 승인 일자
    private Date proStartDate;
    private Date proEndDate;
    private boolean proStatus; // 프로젝트 승인 (Y/N)
    private int proPaidCnt; // 프로젝트 결제 횟수
    private int proNotifyCnt; // 프로젝트 알림신청 횟수
    private String proScript;
    private String sellerName;
    private String sellerDetail; // 프로젝트 판매자 소개글
}
