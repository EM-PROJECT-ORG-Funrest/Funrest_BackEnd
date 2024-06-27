package com.example.app.domain.dto;

import com.example.app.domain.entity.Project;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

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
    private String proPrice; // 프로젝트 가격
    private String proGoalAmount; // 프로젝트 목표 금액
    private Date proDate; // 프로젝트 신청 일자
    private Date proPermitDate; // 프로젝트 승인 일자
    private String proStartDate; // 프로젝트 기간 (시작일자)
    private String proEndDate; // 프로젝트 기간 (종료일자)
    private int proStatus; // 프로젝트 승인 (승인:1 / 미승인:0)
    private int proPaidCnt; // 프로젝트 결제 횟수
    private int proNotifyCnt; // 프로젝트 알림신청 횟수
    private String proScript; // 프로젝트 상세설명
    private String sellerName; // 판매자 이름
    private String sellerDetail; // 프로젝트 판매자 소개글
    private String proDateTime; // 프로젝트 기간 설정
    private int proAchievementRate; // 프로젝트 달성률 구하기
    private String formattedProDate;
    private String proAchievementAmount; //프로젝트 달성금액 구하기
    private long proRemainingDay; // 프로젝트 시작일까지 남은 날짜
    private String mainPageImgPath; // 메인페이지 이미지용

    private List<MultipartFile> proMainImgFile; // projectCreate.html -> Controller 파일 담는 용도
    private List<String> proMainFilePaths;

    private List<MultipartFile> proSubImgFile; // 프로젝트 추가 이미지, projectCreate.html -> Controller 파일 담는 용도
    private List<String> proSubFilePaths;

    // Entity to Dto
    public static ProjectDto toDto(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setProCode(project.getProCode());
        projectDto.setUserId(project.getUserId().getUserId());
        projectDto.setProCategory(project.getProCategory());
        projectDto.setProName(project.getProName());
        projectDto.setProPrice(project.getProPrice());
        projectDto.setProGoalAmount(project.getProGoalAmount());
        projectDto.setProDate(project.getProDate());
        projectDto.setProPermitDate(project.getProPermitDate());
        projectDto.setProStartDate(project.getProStartDate());
        projectDto.setProEndDate(project.getProEndDate());
        projectDto.setProStatus(project.getProStatus());
        projectDto.setProPaidCnt(project.getProPaidCnt());
        projectDto.setProNotifyCnt(project.getProNotifyCnt());
        projectDto.setProScript(project.getProScript());
        projectDto.setSellerName(project.getSellerName());
        projectDto.setSellerDetail(project.getSellerDetail());
        projectDto.setProMainFilePaths(project.getProMainFilePaths());
        projectDto.setProSubFilePaths(project.getProSubFilePaths());
        return projectDto;
    }


}
