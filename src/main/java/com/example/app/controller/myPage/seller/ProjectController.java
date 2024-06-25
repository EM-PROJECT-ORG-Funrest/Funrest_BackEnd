package com.example.app.controller.myPage.seller;

import com.example.app.config.auth.jwt.JwtTokenProvider;
import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.service.project.ProjectServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
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

    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/projectCreate")
    public void projectCreate(HttpServletRequest request) {
        log.info("GET /th/myPage/seller/projectCreate invoke...");
    }

    // projectCreate 페이지에서 ProjectDto 데이터를 받아 DB 에 전송 API (seller 페이지로 리다이렉트)
    @PostMapping("/projectSave")
    public String projectSave(@ModelAttribute ProjectDto projectDto) throws IOException {
        log.info("POST /th/myPage/seller/projectSave invoke.....");
        // System.out.println("ProjectController's ProjectDto : " + projectDto);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        projectDto.setUserId(userId);
        projectServiceImpl.insertProject(projectDto); // Service 단으로 프로젝트 생성 작업 전달

        return "redirect:/th/myPage/seller/seller";
    }


}
