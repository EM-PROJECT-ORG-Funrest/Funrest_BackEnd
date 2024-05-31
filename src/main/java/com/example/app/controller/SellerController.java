package com.example.app.controller;


import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.dto.UserDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
import com.example.app.domain.repository.ProjectRepository;
import com.example.app.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("/th/myPage/seller")
public class SellerController {

    private UserRepository userRepository;

    @GetMapping("/seller")
    public void selectUserID1() {
        log.info("GET /th/myPage/seller/seller .....");
    }


}
