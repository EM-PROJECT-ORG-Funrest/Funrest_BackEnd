package com.example.app.controller.MyPageController.SellerController;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
import com.example.app.domain.service.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/th/myPage/seller")
public class SellerController {

    // 프로젝트 경로 (추후 변경 가능성 있음)
    private static final String UPLOAD_PATH = "http://localhost:8080/upload/";

    @Autowired
    ProjectServiceImpl projectService;


    @GetMapping("/seller")
    public void seller(Model model) {
        //SecurityContextHolder 에서 userId 가져요기
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = new User();
        user.setUserId(userId);
        log.info("GET /th/myPage/seller/seller .....");

        List<ProjectDto> projectDtoList = projectService.findByUserId(user);
        for (int i = 0; i < projectDtoList.size() ; i++) {
            projectDtoList.get(i).setMainPageImgPath(UPLOAD_PATH+projectDtoList.get(i).getStoredFileName().getFirst());
        }
        System.out.println(projectDtoList);
        model.addAttribute("projectDtoList");
    }
}
