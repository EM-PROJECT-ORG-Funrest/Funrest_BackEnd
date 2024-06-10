package com.example.app.controller.Main;

//import com.example.app.domain.dto.ProjectDto;
//import com.example.app.domain.entity.Project;
//import com.example.app.domain.repository.ProjectRepository;
//import com.example.app.domain.service.main.MainServiceImpl;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.List;
//
//@Controller
//@Slf4j
//@RequestMapping("/th/main")
//public class MainController {
//
//    @Autowired
//    public MainServiceImpl mainServiceImpl;
//
//    @GetMapping("/main")
//    public String getProjects(Model model) {
//        List<ProjectDto> projectDtos = mainServiceImpl.getAllProjectsOrderedByProCode();
//        model.addAttribute("projectDtos", projectDtos);
//
//        return "th/main/main"; // Thymeleaf template name
//    }
//
//
//}

import com.example.app.config.WebConfig;
import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.service.main.MainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/th/main")
public class MainController {

    @Autowired
    private MainServiceImpl mainServiceImpl;

    @Autowired
    private WebConfig webConfig;

    @GetMapping("/main")
    public String getProjects(Model model) {
        List<ProjectDto> projectDtos = mainServiceImpl.getAllProjectsOrderedByProCode();
        model.addAttribute("projectDtos", projectDtos.subList(0, Math.min(12, projectDtos.size()))); // 초기 12개 데이터만 전달

        return "th/main/main"; // Thymeleaf template name
    }
}