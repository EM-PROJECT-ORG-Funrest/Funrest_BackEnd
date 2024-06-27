package com.example.app.controller.main;

import com.example.app.config.WebConfig;
import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.service.myPage.SellerServiceImpl;
import com.example.app.domain.service.visit.VisitService;
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

    // 이미지 파일 기본 경로
    private static final String UPLOAD_PATH = "https://funrestbucket.s3.ap-northeast-2.amazonaws.com/";

    @Autowired
    private MainServiceImpl mainServiceImpl;

    @Autowired
    private WebConfig webConfig;

    @Autowired
    private VisitService visitService;

    @Autowired
    private SellerServiceImpl sellerServiceImpl;

    @GetMapping("/main")
    public String getProjects(Model model) {
        List<ProjectDto> projectDtoList = mainServiceImpl.getAllProjectsOrderedByProCode();
        // 프로젝트 참여 인원 가져오기
        sellerServiceImpl.proPaidCnt(projectDtoList);
        for (int i = 0; i < projectDtoList.size(); i++) {
            projectDtoList.get(i).setMainPageImgPath(UPLOAD_PATH + projectDtoList.get(i).getProMainFilePaths().getFirst());
        }
        model.addAttribute("projectDtos", projectDtoList.subList(0, Math.min(12, projectDtoList.size()))); // 초기 12개 데이터만 전달
        // 일 방문자 수 증가
        visitService.recordVisit();

        return "th/main/main"; // Thymeleaf template name
    }
}