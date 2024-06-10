package com.example.app.controller;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.repository.ProjectRepository;
import com.example.app.domain.service.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/th/myPage/seller")
public class ProjectController {

    @Autowired
    private ProjectServiceImpl projectServiceImpl;

    // projectCreate 뷰페이지로 이동 API
    @GetMapping("/projectCreate")
    public void projectCreate() {
        log.info("/th/myPage/seller/projectCreate invoke...");
    }

    // projectCreate 페이지에서 ProjectDto 데이터를 받아 DB 에 전송 API (seller 페이지로 리다이렉트)
    @PostMapping("/projectSave")
    public String projectSave(@ModelAttribute ProjectDto projectDto) throws IOException {
        log.info("/th/myPage/seller/projectSave invoke.....");
        // System.out.println("ProjectController's ProjectDto : " + projectDto);
        projectServiceImpl.insertProject(projectDto); // Service 단으로 프로젝트 생성 작업 전달

        return "redirect:/th/myPage/seller/seller";
    }

    @GetMapping("/seller")
    public void seller(@ModelAttribute ProjectDto projectDto) {
        log.info("GET /th/myPage/seller/seller .....");
    }



}
