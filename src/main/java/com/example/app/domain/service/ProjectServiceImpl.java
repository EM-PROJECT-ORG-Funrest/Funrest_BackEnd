package com.example.app.domain.service;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class ProjectServiceImpl {

    @Autowired
    private ProjectRepository projectRepository;

    @Transactional
    public boolean insertProject(ProjectDto projectDto){

        System.out.println("projectDto : " + projectDto);

        // Dto, Entity 변환 작업은 Service Layer (구현은 Dto,Entity 단)
        // 파일 첨부 여부에 따라 로직 분리
       // if (projectDto.getProMainImg().isEmpty()){
            // 첨부 파일 없음.
            Project project = Project.toSaveEntity(projectDto);
            System.out.println("project Entity : " + project);
            projectRepository.save(project);
        //} else {
            // 첨부 파일 있음.
            /*
             *   1. Dto 에 담긴 파일을 꺼냄
             *   2. 파일의 이름 가져옴
             *   3. 서버 저장용 이름을 만듦
             *   4. 저장 경로 설정
             *   5. 해당 경로에 파일 저장
             *   6. project_table 에 해당 데이터 save 처리
             *   7. project_file_table 에 해당 데이터 save 처리
             * */
           // MultipartFile projectFile = projectDto.getProMainImg(); // proMainImg 의 자료형이 MultipartFile 이기 때문
         //   String originalFilename = projectFile.getOriginalFilename();
       // }

        return true;
    }


    public boolean UpdateProject(ProjectDto projectDto){
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
        System.out.println("UpdateProject method is called.");
        System.out.println(projectDto);
        Project project = Project.toSaveEntity(projectDto);
        System.out.println(project);

        projectRepository.save(project);
        return true;
    }

}
