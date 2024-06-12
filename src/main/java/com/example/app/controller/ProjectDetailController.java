package com.example.app.controller;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.repository.ProjectRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/th/project")
public class ProjectDetailController {

    // 프로젝트 경로 (추후 변경 가능성 있음)
    String UPLOAD_PATH = "http://localhost:8080/upload/";

    @Autowired
    ProjectRepository projectRepository;

    // 프로젝트 상세페이지에 데이터 전송 API
    @GetMapping("/project/{proCode}")
    String project(@PathVariable("proCode") String proCode, Model model) {
        System.out.println(proCode); // 해당 프로젝트 proCode 확인
        Integer projectCode = Integer.parseInt(proCode); // proCode 'int' 형 변환
        Project project = projectRepository.findByProCode(projectCode); // 해당 proCode 의 project 엔터티 행 찾기 및 저장
        ProjectDto projectDto = ProjectDto.toProjectDto(project); // Entity -> Dto

        // ProMainImg 를 리스트에 담기 (3개 고정)
        List<String> storedFileName = projectDto.getStoredFileName();
        // ProMainImg 리스트 -> model 에 담기
        model.addAttribute("Project", projectDto);
        for (int i = 0; i < storedFileName.size(); i++) {
            model.addAttribute("image"+(i+1), UPLOAD_PATH + storedFileName.get(i));
        }

        // ProSubImg 를 리스트에 담기 (5개 고정)
        List<String> subStoredFileName = projectDto.getSubStoredFileName();
        // ProSubImg 리스트 -> model 에 담기
        for (int i = 0; i < subStoredFileName.size(); i++) {
            model.addAttribute("subImage" + (i + 1), UPLOAD_PATH + subStoredFileName.get(i));
        }
        return "th/project/project.html";
    }
}
