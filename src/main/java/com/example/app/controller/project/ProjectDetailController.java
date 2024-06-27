package com.example.app.controller.project;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.repository.ProjectRepository;


import com.example.app.domain.service.project.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/th/project")
public class ProjectDetailController {

    // 이미지 파일 기본 경로
    private static final String UPLOAD_PATH = "https://funrestbucket.s3.ap-northeast-2.amazonaws.com/";

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectServiceImpl projectService;

    // 프로젝트 상세페이지에 데이터 전송 API
    @GetMapping("/project/{proCode}")
    String project(@PathVariable("proCode") String proCode, Model model) {
        Integer projectCode = Integer.parseInt(proCode);
        Project project = projectRepository.findByProCode(projectCode);
        ProjectDto projectDto = ProjectDto.toDto(project);
        // 프로젝트 참여인원 가져오기
        projectService.proPaidCnt(projectDto);
        // 프로젝트 달성 금액 가져오기
        projectService.proAchievementAmount(projectDto);
        // 프로젝트 달성률 가져오기
        projectService.proAchievementRate(projectDto);
        // projectDto 에 proStartDate까지 남은 일자 넣기
        projectService.getProRemainingDay(projectDto);
        model.addAttribute("Project", projectDto);
        // ProMainImg 를 리스트에 담기 (3개 고정)
        List<String> storedFileName = projectDto.getProMainFilePaths();
        for (int i = 0; i < storedFileName.size(); i++) {
            model.addAttribute("image" + (i + 1), UPLOAD_PATH + storedFileName.get(i));
        }
        // ProSubImg 를 리스트에 담기 (5개 고정)
        List<String> subStoredFileName = projectDto.getProSubFilePaths();
        for (int i = 0; i < subStoredFileName.size(); i++) {
            model.addAttribute("subImage" + (i + 1), UPLOAD_PATH + subStoredFileName.get(i));
        }
        return "th/project/project.html";
    }
}
