package com.example.app.domain.service;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.ProjectFile;
import com.example.app.domain.repository.ProjectFileRepository;
import com.example.app.domain.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class ProjectServiceImpl {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectFileRepository projectFileRepository;

    public boolean insertProject(ProjectDto projectDto) throws IOException {
        System.out.println("projectDto : " + projectDto);
        // Dto, Entity 변환 작업은 Service Layer (구현은 Dto,Entity 단)
        // 파일 첨부 여부에 따라 로직 분리
        if (projectDto.getProMainImg().isEmpty()){
            // 첨부 파일 없음.
            Project project = Project.toSaveEntity(projectDto);
//            System.out.println("project Entity : " + project);
            projectRepository.save(project);
        } else {
            // 첨부 파일 있음.
            // 1. Dto 에 담긴 파일을 꺼냄
            MultipartFile projectImgFile = projectDto.getProMainImg(); // proMainImg 의 자료형이 MultipartFile 이기 때문
            // 2. 파일의 이름(확장자까지)  가져옴
            String originalFilename = projectImgFile.getOriginalFilename();
            // 3. 서버 저장용 이름을 만듦 (내사진.jpg => 1812911871_내사진.jpg)
            // currenTimeMillis (UUID 로 대체 가능)
            // : the difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC
            // 1970년 1월 1일 이후로 지금까지 몇 밀리초 까지 지났는 지에 대한 값
            // 0.001초 찰나의 순간 동시에 동일 이름의 파일이 올릴 경우 중복 but, 가능성 매우 희박
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
            // 4. 저장 경로 설정 (해당 경로에 미리 폴더 생성해야함)
            // 윈도우 경우: String savePath = "C:/springboot_img/" + storedFileName; => 결과: C:/springboot_img/17178178127_내사진.jpg
            // 맥 경우: String savePath = "/Users/사용자이름/springboot_img/" + storedFilename; => 결과: C:/springboot_img/17178178127_내사진.jpg
            String savePath = "/Users/hongjaeseong/springboot_img/" + storedFileName;
            // 5. 해당 경로에 파일 저장 (예외 발생 -> 예외 처리 -> 컨트롤러 단에서 예외처리)
            projectImgFile.transferTo(new File(savePath));
            // 6 이전에 tbl_project_file 생성 (Entity 생성)
            // tbl_project (부모) - tbl_project_file (자식)
            // 하나의 프로젝트의 여러개의 파일이 첨부 가능
            // 하나의 파일에 여러개의 프로젝트 불가
            // 네이티브한 쿼리문으로 설명 -> 이 쿼리를 Entity 로 지정해야함
            // create table tbl_project
            // (
            // proCode              int             auto_increment primary key,
            // ...                  ...             ...
            // file_attached        int             null
            // );

            // create table tbl_project_file
            // (
            // id                   int             auto_increment primary key,
            // ...                  ...             ...
            // originalFilename     varchar(255)    null,
            // storedFileName       varchar(255)    null,
            // proCode              int             null,
            // constraint FK_tbl_project_file_tbl_project
            //      foreign key (proCode) references tbl_project (proCode) on delete cascade
            // );
            // Entity 생성하러 ㄱㄱ!

            // 6. tbl_project 에 해당 데이터 save 처리
            // Entity 로 변환
            Project projectEntity = Project.toSaveFileEntity(projectDto);
            // 자식 Entity 에서는 부모 Entity 의 proCode 데이터가 필요
            Integer savedProCode = projectRepository.save(projectEntity).getProCode();
            // projectEntity에는 proCode 가 없기 때문에 DB에 저장 후 proCode를 얻는 후 proCode 를 사용해 해당 project 엔터티를 가져옴
            // Project project = projectRepository.findByProCode(savedProCode).get() 오류 발생;
            Project project = projectRepository.findByProCode(savedProCode).get();
            // 7. tbl_project_file 에 해당 데이터 save 처리
            ProjectFile projectFile = ProjectFile.toProjectFileEntity(project, originalFilename, storedFileName);
            projectFileRepository.save(projectFile);
        }
        return true;
    }

    // proCode 를 기준으로 해당 ProjectDto 조회(파일까지 조회)
    // toProjectDto 에서 부모 엔터티가 자식 엔터티에 접근하고 있어서 트랜잭션 처리 필수!
    @Transactional
    public ProjectDto findByProCode(int proCode){
        Optional<Project> optionalProject = projectRepository.findByProCode(proCode);
        if (optionalProject.isPresent()){
            Project project = optionalProject.get();
            ProjectDto projectDto = ProjectDto.toProjectDto(project);
            return  projectDto;
        } else {
            return null;
        }
    }

    @Transactional
    public List<ProjectDto> findAll(){
        List<Project> projectList = projectRepository.findAll();
        List<ProjectDto> projectDtoList = new ArrayList<>();
        for (Project project : projectList){
            projectDtoList.add(ProjectDto.toProjectDto(project));
        }
        return projectDtoList;
    }


//    public boolean UpdateProject(ProjectDto projectDto){
//        Project project = Project.toSaveEntity(projectDto);
//
//        Project projectSelect = projectRepository.findByProCode(project.getProCode());
//
//        projectSelect.setProCode(project.getProCode());
//        projectSelect.setUserId(project.getUserId());
//        projectSelect.setProCategory(project.getProCategory());
//        projectSelect.setProName(project.getProName());
//        projectSelect.setProPrice(project.getProPrice());
//        projectSelect.setProDate(project.getProDate());
//        projectSelect.setProStartDate(project.getProStartDate());
//        projectSelect.setProEndDate(project.getProEndDate());
//        projectSelect.setProStatus(project.getProStatus());
//        projectSelect.setProPaidCnt(project.getProPaidCnt());
//        projectSelect.setProNotifyCnt(project.getProNotifyCnt());
//        projectSelect.setProScript(project.getProScript());
//        projectSelect.setSellerName(project.getSellerName());
//        projectSelect.setSellerDetail(project.getSellerDetail());
//
//        projectRepository.save(projectSelect);
//
//        return true;
//        System.out.println("UpdateProject method is called.");
//        System.out.println(projectDto);
//        Project project = Project.toSaveEntity(projectDto);
//        System.out.println(project);
//
//        projectRepository.save(project);
//        return true;
//    }

}
