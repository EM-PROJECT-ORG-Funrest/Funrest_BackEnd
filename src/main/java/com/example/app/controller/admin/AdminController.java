package com.example.app.controller.admin;

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

    @GetMapping("/project")
    public void project() {
        log.info("GET /th/admin/project");
    }

    @GetMapping("/project/manage")
    public void manage() {
        log.info("GET /th/admin/project/manage");
    }

    @GetMapping("/member")
    public String adminMember(Model model) {
        log.info("GET /th/admin/member");
        // 1. 전체 회원 정보 조회
        List<UserDto> userDtos = userService.getAllUsers();
        System.out.println("Controller's userDto : " + userDtos);
        // 2. 모델 속성에 정보 저장
        model.addAttribute("users", userDtos);
        // 3. 회원관리 페이지로 넘겨줌
        return "th/admin/member.html";
    }

}
