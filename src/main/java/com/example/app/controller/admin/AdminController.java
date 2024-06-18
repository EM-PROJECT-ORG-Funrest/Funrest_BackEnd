package com.example.app.controller.admin;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.dto.UserDto;
import com.example.app.domain.entity.ProjectFile;
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
    public void adminDashboard() {
        log.info("GET /th/admin/dashboard");
    }

    @GetMapping("/member")
    public void adminMember(Model model) {
        log.info("GET /th/admin/member");
        // 1. 전체 회원 정보 조회
        List<UserDto> userDtos = userService.getAllUsers();
        // 2. 모델 속성에 정보 저장
        model.addAttribute("users", userDtos);
    }

    @GetMapping("/project")
    public void adminProject(Model model) {
        log.info("GET /th/admin/project");
        // 1. 미승인 프로젝트 정보 조회
        List<ProjectDto> projectDtoBeforeList = projectServiceImpl.findByProStatus(0);
        // 2. 승인된 프로젝트 정보 조회
        List<ProjectDto> projectDtoAfterList = projectServiceImpl.findByProStatus(1);
        // 3. 모델 속성에 정보 저장
        model.addAttribute("projectBeforeList", projectDtoBeforeList);
        model.addAttribute("projectAfterList", projectDtoAfterList);
    }

    @GetMapping("/editProject")
    public void adminEditProject(@RequestParam("proCode") String proCode, Model model) {
        log.info("GET /th/admin/editProject/{}", proCode);
        Integer projectCode = Integer.parseInt(proCode);
        // 1. 수정할 프로젝트 정보 조회
        ProjectDto projectDto = projectServiceImpl.findByProCode(projectCode);
        System.out.println("projectDto = " + projectDto);
        // 2. 모델 속성에 정보 저장
        model.addAttribute("project", projectDto);
    }


}
