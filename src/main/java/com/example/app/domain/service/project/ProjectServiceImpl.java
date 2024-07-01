package com.example.app.domain.service.project;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.repository.*;
import com.example.app.domain.service.S3.S3Uploader;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class ProjectServiceImpl {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ImageResizer imageResizer;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private S3Uploader s3Uploader;

    // 프로젝트 생성(만들기)
    @Transactional
    public void createProject(ProjectDto projectDto) throws IOException {
        System.out.println("ProjectServiceImpl's projectDto : " + projectDto);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        projectDto.setUserId(userId);
        projectDto.setProDate(new Date());

        // 파일 첨부 여부에 따라 로직 분리
        // 'ProMainImg 첨부x' and 'ProSubImg 첨부x' 경우
        if ((projectDto.getProMainImgFile() == null || projectDto.getProMainImgFile().isEmpty()
                || projectDto.getProMainImgFile().stream().allMatch(MultipartFile::isEmpty))
                && (projectDto.getProSubImgFile() == null || projectDto.getProSubImgFile().isEmpty()
                || projectDto.getProSubImgFile().stream().allMatch(MultipartFile::isEmpty))) {
            Project project = Project.toEntity(projectDto);
            projectRepository.save(project);
        }
        // 'ProMainImg 첨부o' and 'ProSubImg 첨부o' 경우
        else {
            // 1. 부모 테이블 tbl_project 에 해당 데이터 먼저 저장 처리
            Project project = Project.toEntity(projectDto); // 전달된 dto -> entity 변환
            // 2. 이미지 파일 리사이징 하기
            List<MultipartFile> proMainImgFile = imageResizer.resizeImages(projectDto.getProMainImgFile());
            List<MultipartFile> proSubImgFile = projectDto.getProSubImgFile();
            // 3. 리사이징한 proMainImgFile, proMainSubFile 를 AWS S3 에 담기
            List<String> proMainFilePaths = s3Uploader.saveFiles(proMainImgFile);
            List<String> proSubFilePaths = s3Uploader.saveFiles(proSubImgFile);
            // 4. proMainImgUrl, proSubImgUrl 를 project 엔터티에 담아주기
            project.setProMainFilePaths(proMainFilePaths);
            project.setProSubFilePaths(proSubFilePaths);
            // 5. project 엔터티를 DB 에 저장
            projectRepository.save(project);
        }
    }

    // 특정 proCode 로 프로젝트 조회
    // toProjectDto 에서 부모 엔터티가 자식 엔터티에 접근하고 있어서 트랜잭션 처리 필수!
    @Transactional
    public ProjectDto findByProCode (int proCode) {
        Project optionalProject = projectRepository.findByProCode(proCode);
        Project project = optionalProject;
        ProjectDto projectDto = ProjectDto.toDto(project);
        return projectDto;
    }

    // 모든 프로젝트 조회
    @Transactional
    public List<ProjectDto> findAll() {
        List<Project> projectList = projectRepository.findAll();
        List<ProjectDto> projectDtoList = new ArrayList<>();
        for (Project project : projectList) {
            projectDtoList.add(ProjectDto.toDto(project));
        }
        return projectDtoList;
    }

    // 승인/미승인 프로젝트 조회
    @Transactional
    public Page<ProjectDto> findByProStatus(Integer proStatus, Pageable pageable) {
        Page<Project> projectPage = projectRepository.findByProStatus(proStatus, pageable);
        Page<ProjectDto> projectDtoPage = projectPage.map(ProjectDto::toDto);
        return projectDtoPage;
    }

    // 프로젝트 삭제
    public void deleteProjectsByProCodes(List<Integer> proCodes) {
        System.out.println("proCodes = " + proCodes);
        for (Integer proCode : proCodes) {
            projectRepository.deleteById(proCode);
        }
    }

    // 프로젝트 승인
    public void permitProjectsByProCodes(List<Integer> proCodes) {
        System.out.println("proCodes = " + proCodes);

        for (Integer proCode : proCodes) {
            Optional<Project> optionalProject = projectRepository.findById(proCode);
            optionalProject.ifPresent(project -> {
                // 프로젝트 승인 상태 변경 (미승인,0 -> 승인,1)
                project.setProStatus(1);
                // 현재 날짜 및 시간을 프로젝트 승인 날짜로 설정
                project.setProPermitDate(new Date());
                projectRepository.save(project);
            });
        }
    }

    // 프로젝트 승인 취소
    public void cancelProjectsByProCodes(List<Integer> proCodes) {
        System.out.println("proCodes = " + proCodes);

        for (Integer proCode : proCodes) {
            Optional<Project> optionalProject = projectRepository.findById(proCode);
            optionalProject.ifPresent(project -> {
                // 프로젝트 승인 상태 변경 (승인,1 -> 미승인,0)
                project.setProStatus(0);
                // 승인 날짜 null 값 지정
                project.setProPermitDate(null);
                projectRepository.save(project);
            });
        }
    }

    // 프로젝트 수정
    @Transactional
    public void updateProjectByProCode(int proCode, ProjectDto projectDto) throws IOException {
        // 프로젝트 수정하기 페이지에서 전송된 projectDto 객체
        System.out.println("projectDto = " + projectDto);

        // 파일 첨부 여부에 따라 로직 분리
        // 'ProMainImg 첨부x' and 'ProSubImg 첨부x' 경우
        if ((projectDto.getProMainImgFile() == null || projectDto.getProMainImgFile().isEmpty()
                || projectDto.getProMainImgFile().stream().allMatch(MultipartFile::isEmpty))
                && (projectDto.getProSubImgFile() == null || projectDto.getProSubImgFile().isEmpty()
                || projectDto.getProSubImgFile().stream().allMatch(MultipartFile::isEmpty))) {
            Project projectEntity = Project.toEntity(projectDto);
            // update 하기 위해 proCode 로 기존 optionalProject 엔터티 객체 불러오기
            Optional<Project> optionalProject = projectRepository.findById(proCode);
            optionalProject.ifPresent(project -> {
                // 기존 optionalProject 엔터티에 projectEntity 로 update 해주기
                projectRepository.save(projectEntity);
            });
        }
        // 'ProMainImg 첨부o' and 'ProSubImg 첨부o' 경우
        else {
            // 1. 부모 테이블 tbl_project 에 해당 데이터 먼저 저장 처리
            Project projectEntity = Project.toEntity(projectDto); // 전달된 dto -> entity 변환
            System.out.println("projectEntity : " + projectEntity);
            // update 하기 위해 proCode 로 기존 optionalProject 엔터티 객체 불러오기
            Optional<Project> optionalProject = projectRepository.findById(proCode);
            optionalProject.ifPresent(project -> {
                // 기존 optionalProject 엔터티에 projectEntity 로 update 해주기
                projectRepository.save(projectEntity);
            });
            Integer savedProCode = projectEntity.getProCode(); // entity db에 저장 후 proCode 가져옴
            //Project project = projectRepository.findByProCode(savedProCode); // 가져온 proCode 로 해당하는 부모 엔터티 객체의 데이터를 가져옴
            // 2. 수정하는 proCode 에 해당하는 ProMainImgUrl, ProSubImgUrl 삭제
            projectRepository.deleteProMainImgUrlByProCode(savedProCode);
            projectRepository.deleteProSubImgUrlByProCode(savedProCode);
            // 3. 이미지 파일 리사이징 하기
            List<MultipartFile> proMainImgFile = imageResizer.resizeImages(projectDto.getProMainImgFile());
            List<MultipartFile> proSubImgFile = imageResizer.resizeImages(projectDto.getProSubImgFile());
            // 4. 리사이징한 proMainImgFile, proMainSubFile 를 AWS S3 에 담기
            List<String> proMainFilePaths = s3Uploader.saveFiles(proMainImgFile);
            List<String> proSubFilePaths = s3Uploader.saveFiles(proSubImgFile);
            // 5. proMainImgUrl, proSubImgUrl 를 project 엔터티에 담아주기
            projectEntity.setProMainFilePaths(proMainFilePaths);
            projectEntity.setProSubFilePaths(proSubFilePaths);
            // 6. project 엔터티를 DB 에 저장
            Project project1 = projectRepository.save(projectEntity);
        }
    }

    // 프로젝트 참여인원 가져오기
    public void proPaidCnt(ProjectDto projectDto) {
        int proCode = projectDto.getProCode();
        Optional<Project> optionalProject = projectRepository.findById(proCode);
        Project project = new Project();
        if (optionalProject.isPresent()) {
            project = optionalProject.get();
        }
        Long proPaidCnt = orderRepository.countByProCode(project);
        projectDto.setProPaidCnt(Math.toIntExact(proPaidCnt));
    }

    // 달성 금액 구하기
    public void proAchievementAmount(ProjectDto projectDto) {
        int proPaidCnt = projectDto.getProPaidCnt();
        int proPrice = Integer.parseInt(projectDto.getProPrice().replace(",", ""));
        int proAchievementAmount = proPrice * proPaidCnt;
        // 숫자 포맷팅을 위한 NumberFormat 인스턴스 생성
        NumberFormat format = NumberFormat.getNumberInstance(Locale.getDefault());
        String formattedProAchievementAmount = format.format(proAchievementAmount);
        projectDto.setProAchievementAmount(formattedProAchievementAmount);
    }

    //달성률 구하기
    public void proAchievementRate(ProjectDto projectDto) {
        // 문자열을 정수로 변환
        double proGoalAmount = Double.parseDouble(projectDto.getProGoalAmount().replace(",", ""));
        double proPrice = Double.parseDouble(projectDto.getProPrice().replace(",", ""));
        double proPaidCnt = projectDto.getProPaidCnt();
        // 달성률 계산 (소수점까지 계산)
        double proAchievementRate = (proPrice * proPaidCnt / proGoalAmount) * 100.0;
        // 계산된 달성률을 반올림하여 정수로 변환
        int roundedAchievementRate = (int) Math.round(proAchievementRate);
        // projectDto에 달성률 설정
        projectDto.setProAchievementRate(roundedAchievementRate);
    }

    // proStartDate 에 따른 버튼 구현을 위한 디데이 구하기
    public void getProRemainingDay(ProjectDto projectDto) {
        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        LocalDate target = LocalDate.parse(projectDto.getProStartDate(), DATE_FORMATTER);
        long remainingDays = java.time.temporal.ChronoUnit.DAYS.between(today, target);
        projectDto.setProRemainingDay(remainingDays);
    }




}
