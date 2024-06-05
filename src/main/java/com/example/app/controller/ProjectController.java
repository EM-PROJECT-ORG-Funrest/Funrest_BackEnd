package com.example.app.controller;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
import com.example.app.domain.repository.ProjectRepository;
import com.example.app.domain.service.ProjectServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.IOException;

@Controller
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/th/myPage/seller")
public class ProjectController {

    // 프로젝트 관련 비즈니스 로직을 구현할 객체 주입
    @Autowired
    private ProjectServiceImpl projectServiceImpl;
    @Autowired
    private ProjectRepository projectRepository;

    // projectCreate 페이지로 이동
    @GetMapping("/projectCreate")
    public void projectCreate() {
        log.info("/th/myPage/seller/projectCreate invoke...");
    }

    // projectCreate 페이지에서 ProjectDto 데이터를 받아 DB 에 저장 후 seller 페이지로 리다이렉트
    @PostMapping("/projectSave")
    public String projectSave(@ModelAttribute ProjectDto projectDto, Model model) throws IOException {
        log.info("/th/myPage/seller/projectSave invoke.....");
        System.out.println("ProjectController's ProjectDto : " + projectDto);

        projectServiceImpl.insertProject(projectDto); // Service 단으로 프로젝트 생성 작업 전달

        return "redirect:/th/myPage/seller/seller";
    }

    @GetMapping("/seller")
    public void seller(@ModelAttribute ProjectDto projectDto) {
        log.info("GET /th/myPage/seller/seller .....");

    }



}
