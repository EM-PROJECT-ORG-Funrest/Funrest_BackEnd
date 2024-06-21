package com.example.app.domain.service;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.ProjectFile;
import com.example.app.domain.entity.ProjectSubFile;
import com.example.app.domain.entity.User;
import com.example.app.domain.repository.ProjectFileRepository;
import com.example.app.domain.repository.ProjectRepository;
import com.example.app.domain.repository.ProjectSubFileRepository;
import com.example.app.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
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
    private ProjectFileRepository projectFileRepository;

    @Autowired
    private ProjectSubFileRepository projectSubFileRepository;

    @Autowired
    private ProjectImgFileServiceImpl projectImgFileService;

    @Autowired
    private UserRepository userRepository;

    // 프로젝트 생성(만들기)
    public void insertProject(ProjectDto projectDto) throws IOException {
        System.out.println("ProjectServiceImpl's projectDto : " + projectDto);
        // Service Layer 에서 Dto, Entity 변환 작업 (구현은 Dto,Entity 단)

        // 파일 첨부 여부에 따라 로직 분리
        // 'ProMainImg 첨부x' and 'ProSubImg 첨부x' 경우
        if ((projectDto.getProMainImg() == null || projectDto.getProMainImg().isEmpty()
                || projectDto.getProMainImg().stream().allMatch(MultipartFile::isEmpty))
                && (projectDto.getProSubImg() == null || projectDto.getProSubImg().isEmpty()
                || projectDto.getProSubImg().stream().allMatch(MultipartFile::isEmpty)))
        {
            Project project = Project.toSaveEntity(projectDto);
            System.out.println("project : " + project);
            project.setProDate(new Date()); // proDate에 현재날짜 넣어주기
            projectRepository.save(project);
        }
        // 'ProMainImg 첨부o' and 'ProSubImg 첨부o' 경우
        else {
            // 1. 부모 테이블 tbl_project 에 해당 데이터 먼저 저장 처리
            Project projectEntity = Project.toSaveFileEntity(projectDto); // 전달된 dto -> entity 변환
            projectEntity.setProDate(new Date()); // proDate에 현재날짜 넣어주기
            System.out.println("projectEntity : " + projectEntity);
            Integer savedProCode = projectRepository.save(projectEntity).getProCode(); // entity db에 저장 후 proCode 가져옴
            Project project = projectRepository.findByProCode(savedProCode); // 가져온 proCode 로 해당하는 부모 엔터티 객체의 데이터를 가져옴
            // 2. projectDto 에 담긴 다중 ProMainImg 차례로 꺼내서 proMainImgFile 에 담기
            for (MultipartFile proMainImgFile : projectDto.getProMainImg()) // proMainImg 가 여러 개이기 때문에 반복문 작성
            {
                // 2-1. 파일의 이름 가져오기 및 저장
                String originalFilename = proMainImgFile.getOriginalFilename();
                // 2-2. 서버 저장용 이름 생성 (내사진.jpg => 1812911871_내사진.jpg)
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
                // 2-3. 저장 경로 설정 (헤당 경로에 미리 폴더 생성하기)
                // 윈도우 경우: String savePath = "C:/springboot_img/" + storedFileName; => 결과: C:/springboot_img/17178178127_내사진.jpg
                // 맥 경우: String savePath = "/Users/사용자이름/springboot_img/" + storedFilename; => 결과: C:/springboot_img/17178178127_내사진.jpg
                String savePath = "C:/springboot_img/" + storedFileName;
                // 2-4. 이미지 파일 리사이징 및 저장 경로에 저장 메소드 호출
                projectImgFileService.uploadFile(proMainImgFile, savePath);
                // 2-5. tbl_project_file 에 해당 데이터 저장 처리
                ProjectFile projectFile = ProjectFile.toProjectFileEntity(project, originalFilename, storedFileName);
                projectFileRepository.save(projectFile);
            }
            // 3. projectDto 에 담긴 다중 ProSubImg 차례로 꺼내서 proSubImgFile 에 담기
            for (MultipartFile proSubImgFile : projectDto.getProSubImg()) // proSubImg 가 여러 개이기 때문에 반복문 작성
            {
                // 3-1. 파일의 이름 가져오기 및 저장
                String subOriginalFileName = proSubImgFile.getOriginalFilename();
                // 3-2. 서버 저장용 이름 생성
                String subStoredFileName = System.currentTimeMillis() + "_" + subOriginalFileName;
                // 3-3. 저장 경로 설정 (해당 경로에 미디 폴더 생성하기)
                String subSavePath = "C:/springboot_subImg/" + subStoredFileName;
                // 3-4. 해당 경로에 파일 저장
                proSubImgFile.transferTo(new File(subSavePath));
                // 3-5. tbl_project_subFile 에 해당 데이터 저장 처리
                ProjectSubFile projectSubFile = ProjectSubFile.toProjectSubFileEntity(project, subOriginalFileName, subStoredFileName);
                projectSubFileRepository.save(projectSubFile);
            }
        }

    }

    //달성률 구하기
    public void proAchievementRate(ProjectDto projectDto) {
        //달성률 구하기
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

    // 달성 금액 구하기
    public void proAchievementAmount(ProjectDto projectDto){
        int proPrice = Integer.parseInt(projectDto.getProPrice().replace(",", ""));
        int proPaidCnt = projectDto.getProPaidCnt();

        int proAchievementAmount = proPrice * proPaidCnt;

        // 숫자 포맷팅을 위한 NumberFormat 인스턴스 생성
        NumberFormat format = NumberFormat.getNumberInstance(Locale.getDefault());
        String formattedProAchievementAmount = format.format(proAchievementAmount);

        projectDto.setProAchievementAmount(formattedProAchievementAmount);


    }

    // toProjectDto 에서 부모 엔터티가 자식 엔터티에 접근하고 있어서 트랜잭션 처리 필수!
    @Transactional
    public ProjectDto findByProCode(int proCode){
        Project optionalProject = projectRepository.findByProCode(proCode);
        Project project = optionalProject;
        ProjectDto projectDto = ProjectDto.toProjectDto(project);
        return  projectDto;
    }

    @Transactional
    public List<ProjectDto> findAll() {
        List<Project> projectList = projectRepository.findAll();
        List<ProjectDto> projectDtoList = new ArrayList<>();
        for (Project project : projectList) {
            projectDtoList.add(ProjectDto.toProjectDto(project));
        }
        return projectDtoList;
    }

    // 승인/미승인 프로젝트 검색
    @Transactional
    public Page<ProjectDto> findByProStatus(Integer proStatus, Pageable pageable) {
        Page<Project> projectPage = projectRepository.findByProStatus(proStatus, pageable);
        Page<ProjectDto> projectDtoPage = projectPage.map(ProjectDto::toProjectDto);
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
        if ((projectDto.getProMainImg() == null || projectDto.getProMainImg().isEmpty()
                || projectDto.getProMainImg().stream().allMatch(MultipartFile::isEmpty))
                && (projectDto.getProSubImg() == null || projectDto.getProSubImg().isEmpty()
                || projectDto.getProSubImg().stream().allMatch(MultipartFile::isEmpty))) {
            Project projectEntity = Project.toSaveEntity(projectDto);
            System.out.println("projectEntity : " + projectEntity);
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
            Project projectEntity = Project.toSaveFileEntity(projectDto); // 전달된 dto -> entity 변환
            System.out.println("projectEntity : " + projectEntity);
            // update 하기 위해 proCode 로 기존 optionalProject 엔터티 객체 불러오기
            Optional<Project> optionalProject = projectRepository.findById(proCode);
            optionalProject.ifPresent(project -> {
                // 기존 optionalProject 엔터티에 projectEntity 로 update 해주기
                projectRepository.save(projectEntity);
            });
            Integer savedProCode = projectEntity.getProCode(); // entity db에 저장 후 proCode 가져옴
            Project project = projectRepository.findByProCode(savedProCode); // 가져온 proCode 로 해당하는 부모 엔터티 객체의 데이터를 가져옴
            // 2. 수정하는 proCode 에 해당하는 projectFile 객체 & projectSubFile 객체 삭제
            projectFileRepository.deleteByProject_proCode(savedProCode);
            projectSubFileRepository.deleteByProject_proCode(savedProCode);
            // 3. projectDto 에 담긴 다중 ProMainImg 차례로 꺼내서 proMainImgFile 에 담기
            for (MultipartFile proMainImgFile : projectDto.getProMainImg()) // proMainImg 가 여러 개이기 때문에 반복문 작성
            {
                // 3-1. 파일의 이름 가져오기 및 저장
                String originalFilename = proMainImgFile.getOriginalFilename();
                // 3-2. 서버 저장용 이름 생성 (내사진.jpg => 1812911871_내사진.jpg)
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
                // 3-3. 저장 경로 설정 (헤당 경로에 미리 폴더 생성하기)
                // 윈도우 경우: String savePath = "C:/springboot_img/" + storedFileName; => 결과: C:/springboot_img/17178178127_내사진.jpg
                // 맥 경우: String savePath = "/Users/사용자이름/springboot_img/" + storedFilename; => 결과: C:/springboot_img/17178178127_내사진.jpg
                String savePath = "C:/springboot_img/" + storedFileName;
                // 3-4. 이미지 파일 리사이징 및 저장 경로에 저장 메소드 호출
                projectImgFileService.uploadFile(proMainImgFile, savePath);
                // 3-5. tbl_project_file 에 해당 데이터 수정 처리
                ProjectFile projectFile = ProjectFile.toProjectFileEntity(project, originalFilename, storedFileName);
                projectFileRepository.save(projectFile);
            }
            // 4. projectDto 에 담긴 다중 ProSubImg 차례로 꺼내서 proSubImgFile 에 담기
            for (MultipartFile proSubImgFile : projectDto.getProSubImg()) // proSubImg 가 여러 개이기 때문에 반복문 작성
            {
                // 4-1. 파일의 이름 가져오기 및 저장
                String subOriginalFileName = proSubImgFile.getOriginalFilename();
                // 4-2. 서버 저장용 이름 생성
                String subStoredFileName = System.currentTimeMillis() + "_" + subOriginalFileName;
                // 4-3. 저장 경로 설정 (해당 경로에 미디 폴더 생성하기)
                String subSavePath = "C:/springboot_subImg/" + subStoredFileName;
                // 4-4. 해당 경로에 파일 저장
                proSubImgFile.transferTo(new File(subSavePath));
                // 4-5. tbl_project_subFile 에 해당 데이터 저장 처리
                ProjectSubFile projectSubFile = ProjectSubFile.toProjectSubFileEntity(project, subOriginalFileName, subStoredFileName);
                projectSubFileRepository.save(projectSubFile);
            }
        }


    }



}