package com.example.app.controller;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.service.ProjectServiceImpl;
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
@RequestMapping("/th")
public class ProjectController {

    // 프로젝트 관련 비즈니스 로직을 구현할 객체 주입
    @Autowired
    private ProjectServiceImpl projectServiceImpl;


    // projectCreate 페이지로 GetMapping (확인용 로깅)
    @GetMapping("/myPage/seller/projectCreate")
    public void projectCreate(){
        log.info("/th/myPage/seller/projectCreate invoke...");
    }

    // projectCreate 페이지에서 프로젝트 데이터를 받아 DB에 저장 후 seller 페이지로 리다이렉트
    @PostMapping("/myPage/seller/projectCreateCallBack")
    public String projectCreateCallBack(@ModelAttribute ProjectDto projectDto){
        System.out.println("ProjectDto : " + projectDto);
        log.info("/myPage/seller/projectCreateCallBack invoke.....");
        projectServiceImpl.insertProject(projectDto);
//        System.out.println(projectDto.getProStartDate());
//        System.out.println(projectDto.getProEndDate());

        return "redirect:/th/myPage/seller/seller";
    }
}
