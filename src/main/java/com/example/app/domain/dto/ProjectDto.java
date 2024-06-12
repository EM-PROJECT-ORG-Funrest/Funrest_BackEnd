package com.example.app.domain.dto;

import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.ProjectFile;
import com.example.app.domain.entity.ProjectSubFile;
import com.example.app.domain.entity.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
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

    private List<MultipartFile> proMainImg; // projectCreate.html -> Controller 파일 담는 용도
    private List<String> originalFileName; // 원본 파일 이름 // 아래 3가지는 서비스단에서 처리
    private List<String> storedFileName; // 서버 저장용 파일 이름 (같은 이름의 파일이름을 구분하기 위한 용도)
    private int fileAttached; // 파일 첨부 여부 (첨부 1, 미첨부 0), boolean 타입은 작업이 상대적으로 복잡
    private String mainPageImgPath; // 메인페이지 이미지용

    private List<MultipartFile> proSubImg; // 프로젝트 추가 이미지, projectCreate.html -> Controller 파일 담는 용도
    private List<String> subOriginalFileName; // 원본 파일 이름
    private List<String> subStoredFileName; // 서버 저장용 파일 이름
    private int subFileAttached; // 파일 첨부 여부 (첨부 1, 미첨부 0)

    private String proPrice; // 프로젝트 가격
    private String proGoalAmount; // 프로젝트 목표 금액
    private Date proDate; // 프로젝트 승인 일자 (관리자 페이지 구현 초기 전엔 프로젝트 생성 일자로 통일)
    private String proStartDate; // 프로젝트 기간 (시작일자)
    private String proEndDate; // 프로젝트 기간 (종료일자)
    private int proStatus; // 프로젝트 승인 (승인:1 / 미승인:0)
    private int proPaidCnt; // 프로젝트 결제 횟수
    private int proNotifyCnt; // 프로젝트 알림신청 횟수
    private String proScript; // 프로젝트 상세설명
    private String sellerName; // 판매자 이름
    private String sellerDetail; // 프로젝트 판매자 소개글
    private String datetime; //

    // Entity to Dto
    public static ProjectDto toProjectDto(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setProCode(project.getProCode());
        projectDto.setUserId(project.getUserId().getUserId());
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
        if (project.getFileAttached() == 0)
        // 0, proMainImg 파일 없음
        {
            projectDto.setFileAttached(project.getFileAttached());
        }
        else
        // 1, proMainImg 파일 있음
        {
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
            projectDto.setFileAttached(project.getFileAttached());
            for (ProjectFile projectFile : project.getProjectFileList())
            // 리스트형 originalFileName 에 다중 값을 넣어주기 위해 반복문으로 list 에 넣기
            // 리스트형 storedFileName 에 다중 값을 넣어주기 위해 반복문으로 list 에 넣기
            {
                originalFileNameList.add(projectFile.getOriginalFileName());
                storedFileNameList.add(projectFile.getStoredFileName());
            }
            projectDto.setOriginalFileName(originalFileNameList);
            projectDto.setStoredFileName(storedFileNameList);
        }
        if (project.getSubFileAttached() == 0)
        // 0, proSubFile 파일 없음
        {
            projectDto.setSubFileAttached(project.getSubFileAttached());
        }
        else
        // 1, proSubFile 파일 있음
        {
            List<String> subOriginalFileNameList = new ArrayList<>();
            List<String> subStoredFileNameList = new ArrayList<>();
            projectDto.setSubFileAttached(project.getSubFileAttached());
            for (ProjectSubFile projectSubFile : project.getProjectSubFileList())
            {
                subOriginalFileNameList.add(projectSubFile.getSubOriginalFileName());
                subStoredFileNameList.add(projectSubFile.getSubStoredFileName());
            }
            projectDto.setSubStoredFileName(subOriginalFileNameList);
            projectDto.setSubStoredFileName(subStoredFileNameList);
        }
        return projectDto;
    }


}
