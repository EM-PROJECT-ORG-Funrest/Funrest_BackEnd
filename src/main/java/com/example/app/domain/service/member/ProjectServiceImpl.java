package com.example.app.domain.service.member;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public boolean ParseDate(ProjectDto projectDto) throws ParseException {
        SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");


        // projectDto.getProStartDate()와 projectDto.getProEndDate()가 String 에서 Date로 변환

        Date proStartDate = inputFormatter.parse((String) projectDto.getProStartDate());
        String proStartDateStr = outputFormatter.format(proStartDate);

        Date proEndDate = inputFormatter.parse((String) projectDto.getProEndDate());
        String proEndDateStr = outputFormatter.format(proEndDate);

        // datetime 즉 달력 input칸에 넣어 줄 데이터 생성
        String datetime = proStartDateStr + " - " + proEndDateStr;
        projectDto.setDatetime(datetime);

        return true;
    }

    public boolean UpdateProject(ProjectDto projectDto)  {

        SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd");

        // String 형으로 일단 양식에 맞게 잘라주기 yyyy-MM-dd 형태로 잘라줌
        String proStartDateStr = projectDto.getDatetime().substring(0,10);
        String proEndDateStr = projectDto.getDatetime().substring(13,23);

        // 잘라 준 형태의 String 값을 Dto에 삽입
        projectDto.setProStartDate(proStartDateStr);
        projectDto.setProEndDate(proEndDateStr);

        // 문자열을 Date 객체로 변환 과정
        Date proStartDate = null;
        Date proEndDate = null;

        // parsing 작업 project에 넣어 주기 위해 한거
        try {
            proStartDate = inputFormatter.parse(proStartDateStr);
            proEndDate = inputFormatter.parse(proEndDateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // ProjectDto에 넣어주는건데 굳이 필요하진 않음
        projectDto.setProStartDate(proStartDateStr); // 여전히 String으로 설정
        projectDto.setProEndDate(proEndDateStr); // 여전히 String으로 설정

        // Project 엔티티 변환 및 저장 toSaveEntity 추가 했음 Project에는 이때 Date 값으로 넣음....
        Project project = Project.toSaveEntity(projectDto, proStartDate, proEndDate);
        // dirty checking 사용
        projectRepository.save(project);

        return true;
    }

}
