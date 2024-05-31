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
    private MultipartFile proMainImg; // projectCreate.html -> Controller 파일 담는 용도
    private String originalFileName; // 원본 파일 이름 // 아래 3가지는 서비스단에서 처리
    private String storedFileName; // 서버 저장용 파일 이름 (같은 이름의 파일이름을 구분하기 위한 용도)
    private int fileAttached; // 파일 첨부 여부 (첨부 1, 미첨부 0), boolean 타입은 작업이 상대적으로 복잡
    // 다중 이미지 파일
    // private MultipartFile proImg; // 프로젝트 추가 이미지
    private String proPrice; // 프로젝트 가격
    private String proGoalAmount; // 프로젝트 목표 금액
    private Date proDate; // 프로젝트 승인 일자 (관리자 페이지 구현 초기 전엔 프로젝트 생성 일자로 통일)
    private Date proStartDate; // 프로젝트 기간 (시작일자)
    private Date proEndDate; // 프로젝트 기간 (종료일자)
    private int proStatus; // 프로젝트 승인 (승인:1 / 미승인:0)
    private int proPaidCnt; // 프로젝트 결제 횟수
    private int proNotifyCnt; // 프로젝트 알림신청 횟수
    private String proScript; // 프로젝트 상세설명
    private String sellerName; // 판매자 이름
    private String sellerDetail; // 프로젝트 판매자 소개글

    // Entity to Dto
    public static ProjectDto toProjectDto(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setProCode(project.getProCode());
        projectDto.setUserId(project.getUserId());
        projectDto.setProCategory(project.getProCategory());
        projectDto.setProName(project.getProName());
        projectDto.setProPrice(project.getProPrice());
        projectDto.setProGoalAmount(project.getProGoalAmount());
        projectDto.setProDate(project.getProDate());
        projectDto.setProStartDate(project.getProStartDate());
        projectDto.setProEndDate(project.getProEndDate());
        projectDto.setProStatus(project.getProStatus());
        projectDto.setProPaidCnt(project.getProPaidCnt());
        projectDto.setProNotifyCnt(project.getProNotifyCnt());
        projectDto.setProScript(project.getProScript());
        projectDto.setSellerName(project.getSellerName());
        projectDto.setSellerDetail(project.getSellerDetail());
        if (project.getFileAttached() == 0) {
            projectDto.setFileAttached(project.getFileAttached()); // 0, 파일 없음
        } else {
            projectDto.setFileAttached(project.getFileAttached()); // 1, 파일 있음
            // 뷰페이지에서 사용자가 업로드한 파일(이미지)의 정보를 확인하기 위해 이름도 가져가야함
            // originalFileName, storedFileName 의 위치: tbl_project_file (ProjectFile)
            // 서로 다른 테이블에 담겨 있는 값을 가져올 때는 join 문법을 사용해야함
            // join
            // select * from tbl_project p, tbl_project_file pf where p.proCode = pf.proCode
            // and where p.id = ?;
            // but, 이것을 Spring JPA 가 join 문법을 사용하는 대신
            // Project 엔터티와 ProjectFile 엔터티간 연관 관계를 맺어
            // 부모 엔터티 객체가 하위 엔터티 객체의 값을 직접적으로 사용할 수 있다.
            // get(0): ProjectFileList 리스트에 하나의 값만 들어있기 때문에 Index 를 '0'으로 설정, 첨부파일이 여러개일때 반복문 사용
            projectDto.setOriginalFileName(project.getProjectFileList().get(0).getOriginalFileName());
            projectDto.setStoredFileName(project.getProjectFileList().get(0).getStoredFileName());
        }

        return projectDto;

    }


    //Entity To Dto
//    public static ProjectDto ToDto(Project project) {
//        return ProjectDto.builder()
//                .proCode(project.getProCode())
//                .userId(project.getUserId())
//                .proCategory(project.getProCategory())
//                .proName(project.getProName())
////                .proImg(project.getProImg())
////                .proMainImg(project.getProMainImg())
//                .proPrice(project.getProPrice())
//                .proDate(project.getProDate())
//                .proStartDate(project.getProStartDate())
//                .proEndDate(project.getProEndDate())
//                .proStatus(project.getProStatus())
//                .proPaidCnt(project.getProPaidCnt())
//                .proNotifyCnt(project.getProNotifyCnt())
//                .proScript(project.getProScript())
//                .sellerName(project.getSellerName())
//                .sellerDetail(project.getSellerDetail())
//                .build();
//    }
}
