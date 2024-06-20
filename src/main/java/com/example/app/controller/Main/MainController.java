package com.example.app.controller.Main;
import com.example.app.config.WebConfig;
import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.service.main.MainServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
    


@Slf4j
@Controller
@RequestMapping("/th/main")
public class MainController {

    // 프로젝트 경로 (추후 변경 가능성 있음)
    String UPLOAD_PATH = "http://localhost:8080/upload/";

    @Autowired
    private MainServiceImpl mainServiceImpl;

    @Autowired
    private WebConfig webConfig;
    
    @GetMapping("/main")
    public void getProjects(Model model) {
        log.info("GET /th/main/main invoked....");
    
//        List<ProjectDto> projectDtos = mainServiceImpl.getAllProjectsOrderedByProCode();
//        for (int i = 0; i < projectDtos.size() ; i++) {
//            projectDtos.get(i).setMainPageImgPath(UPLOAD_PATH+projectDtos.get(i).getStoredFileName().getFirst());
//        }
//        model.addAttribute("projectDtos", projectDtos.subList(0, Math.min(12, projectDtos.size()))); // 초기 12개 데이터만 전달
//
//
//        return "th/main/main"; // Thymeleaf template name
        }
    }