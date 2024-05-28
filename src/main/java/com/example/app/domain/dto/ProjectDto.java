package com.example.app.domain.dto;

import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
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
    private User userId;
    private String proCategory;
    private String proName;
    private String proImg; // 프로젝트 추가 이미지
    private String proMainImg; // 프로젝트 썸네일 이미지
    private int proPrice;
    private Date proDate; // 프로젝트 승인 일자 (관리자 페이지 구현 초기 전엔 프로젝트 생성 일자로 통일)
    private Date proStartDate; // 프로젝트 기간 (시작일자)
    private Date proEndDate; // 프로젝트 기간 (종료일자)
    private String proStatus; // 프로젝트 승인 (승인 / 승인 대기 / 기간 만료..? / 승인 취소)
    private int proPaidCnt; // 프로젝트 결제 횟수
    private int proNotifyCnt; // 프로젝트 알림신청 횟수
    private String proScript; // 프로젝트 상세설명
    private String sellerName; // 판매자 이름
    private String sellerDetail; // 프로젝트 판매자 소개글

    public static ProjectDto EntityToProjectDto(Project project) {
        return ProjectDto.builder()
                .proCode(project.getProCode())
                .userId(project.getUserId())
                .proCategory(project.getProCategory())
                .proName(project.getProName())
                .proImg(project.getProImg())
                .proMainImg(project.getProMainImg())
                .proPrice(project.getProPrice())
                .proDate(project.getProDate())
                .proStartDate(project.getProStartDate())
                .proEndDate(project.getProEndDate())
                .proStatus(project.getProStatus())
                .proPaidCnt(project.getProPaidCnt())
                .proNotifyCnt(project.getProNotifyCnt())
                .proScript(project.getProScript())
                .sellerName(project.getSellerName())
                .sellerDetail(project.getSellerDetail())
                .build();
    }
}
