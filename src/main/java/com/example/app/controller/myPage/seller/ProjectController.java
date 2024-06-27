package com.example.app.controller.myPage.seller;

import com.example.app.config.auth.jwt.JwtTokenProvider;
import com.example.app.domain.service.project.ProjectServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

}
