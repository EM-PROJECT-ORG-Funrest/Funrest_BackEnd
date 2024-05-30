package com.example.app.domain.dto;

import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    // 단일 이미지 파일
    //private MultipartFile proMainImg; // 프로젝트 썸네일 이미지 // projectCreate.html -> Controller 파일 담는 용도
    private String originalFileName; // 원본 파일 이름
    private String storedFileName; // 서버 저장용 파일 이름 (같은 이름의 파일이름을 구분하기 위한 용도)
    private int fileAttached; // 파일 첨부 여부 (첨부 1, 미첨부 0)

//    private MultipartFile proImg; // 프로젝트 추가 이미지

    private String proPrice;
    private Date proDate; // 프로젝트 승인 일자 (관리자 페이지 구현 초기 전엔 프로젝트 생성 일자로 통일)
    private Date proStartDate; // 프로젝트 기간 (시작일자)
    private Date proEndDate; // 프로젝트 기간 (종료일자)
    private int proStatus; // 프로젝트 승인 (승인:1 / 미승인:0)
    private int proPaidCnt; // 프로젝트 결제 횟수
    private int proNotifyCnt; // 프로젝트 알림신청 횟수
    private String proScript; // 프로젝트 상세설명
    private String sellerName; // 판매자 이름
    private String sellerDetail; // 프로젝트 판매자 소개글

  //   Entity To Dto
    public static ProjectDto ToDto(Project project) {
        return ProjectDto.builder()
                .proCode(project.getProCode())
                .userId(project.getUserId())
                .proCategory(project.getProCategory())
                .proName(project.getProName())
//                .proImg(project.getProImg())
//                .proMainImg(project.getProMainImg())
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
