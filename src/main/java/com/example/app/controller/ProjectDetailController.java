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
        model.addAttribute("image1", UPLOAD_PATH + storedFileName.get(0)); // String 클래스자료형으로 데이터 전송
        model.addAttribute("image2", UPLOAD_PATH + storedFileName.get(1));
        model.addAttribute("image3", UPLOAD_PATH + storedFileName.get(2));

        // ProSubImg 를 리스트에 담기 (5개 고정)
        List<String> subStoredFileName = projectDto.getSubStoredFileName();
        // ProSubImg 리스트 -> model 에 담기
        model.addAttribute("subImage1", UPLOAD_PATH + subStoredFileName.get(0));
        model.addAttribute("subImage2", UPLOAD_PATH + subStoredFileName.get(1));
        model.addAttribute("subImage3", UPLOAD_PATH + subStoredFileName.get(2));
        model.addAttribute("subImage4", UPLOAD_PATH + subStoredFileName.get(3));
        model.addAttribute("subImage5", UPLOAD_PATH + subStoredFileName.get(4));

        return "th/project/project.html";
    }
}
