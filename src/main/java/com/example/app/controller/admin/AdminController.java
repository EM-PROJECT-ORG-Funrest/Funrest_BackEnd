package com.example.app.controller.admin;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.dto.UserDto;
import com.example.app.domain.service.ProjectServiceImpl;
import com.example.app.domain.service.member.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/th/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    ProjectServiceImpl projectServiceImpl;

    @GetMapping("/dashboard")
    public void dashboard() {
        log.info("GET /th/admin/dashboard");
    }

    @GetMapping("/member")
    public String adminMember(Model model) {
        log.info("GET /th/admin/member");
        // 1. 전체 회원 정보 조회
        List<UserDto> userDtos = userService.getAllUsers();
        // 2. 모델 속성에 정보 저장
        model.addAttribute("users", userDtos);
        // 3. 회원 관리 페이지로 전달
        return "th/admin/member.html";
    }

    @GetMapping("/project")
    public String project(Model model) {
        log.info("GET /th/admin/project");
        // 1. 미승인 프로젝트 정보 조회
        List<ProjectDto> projectDtoBeforeList = projectServiceImpl.findByProStatus(0);
        // 2. 승인된 프로젝트 정보 조회
        List<ProjectDto> projectDtoAfterList = projectServiceImpl.findByProStatus(1);
        // 3. 모델 속성에 정보 저장
        model.addAttribute("projectBeforeList", projectDtoBeforeList);
        model.addAttribute("projectAfterList", projectDtoAfterList);
        // 3. 프로젝트 관리 페이지로 전달
        return "th/admin/project.html";
    }

    @GetMapping("/project/manage")
    public void manage() {
        log.info("GET /th/admin/project/manage");
    }



}
