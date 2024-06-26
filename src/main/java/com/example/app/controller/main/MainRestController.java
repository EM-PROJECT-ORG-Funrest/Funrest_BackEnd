package com.example.app.controller.main;


import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.service.main.MainServiceImpl;
import com.example.app.domain.service.myPage.SellerServiceImpl;
import com.example.app.domain.service.notify.NotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/th/main/main")
@Slf4j
public class MainRestController {

    // 이미지 파일 기본 경로
    private static final String UPLOAD_PATH = "https://funrestbucket.s3.ap-northeast-2.amazonaws.com/";

    private final MainServiceImpl mainService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private SellerServiceImpl sellerServiceImpl;


    @Autowired
    public MainRestController(MainServiceImpl mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/projects")
    public Page<ProjectDto> getProjects(@RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "size", defaultValue = "12") int size) {
        // 프로젝트를 페이지별로 검색하여 반환
        Page<ProjectDto> projectDtoPage = mainService.getAllProjectsOrderedByProCode(PageRequest.of(page, size));
        List<ProjectDto> projectDtoList = projectDtoPage.getContent();
        sellerServiceImpl.proPaidCnt(projectDtoList);
        // 각 ProjectDto에 이미지 URL을 설정
        projectDtoPage.forEach(projectDto -> {
            if (!projectDto.getProMainFilePaths().isEmpty()) {
                projectDto.setMainPageImgPath(UPLOAD_PATH + projectDto.getProMainFilePaths().get(0));
            } else {
                projectDto.setMainPageImgPath(""); // 이미지가 없는 경우 처리
            }
        });
        return projectDtoPage;
    }

    @GetMapping("/category")
    public Page<ProjectDto> getProjectsByCategory(@RequestParam(value = "proCategory", defaultValue = "all") String proCategory,
                                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "12") int size,
                                                  Model model) {
        log.info("getProjectByCategory : " + proCategory + ", page : " + page + ", size : " + size);
        Page<ProjectDto> projectDtoPage = null;
        // 카테고리 들어오는 값 별로 나누기
        if (proCategory.equals("all")) {
            // 프로젝트를 페이지별로 검색하여 반환
            projectDtoPage = mainService.getAllProjectsOrderedByProCode(PageRequest.of(page, size));
            List<ProjectDto> projectDtoList = projectDtoPage.getContent();
            sellerServiceImpl.proPaidCnt(projectDtoList);
            // 각 ProjectDto에 이미지 URL을 설정
            projectDtoPage.forEach(projectDto -> {
                if (!projectDto.getProMainFilePaths().isEmpty()) {
                    projectDto.setMainPageImgPath(UPLOAD_PATH + projectDto.getProMainFilePaths().get(0));
                } else {
                    projectDto.setMainPageImgPath(""); // 이미지가 없는 경우 처리
                }
            });
            return projectDtoPage;

        } else if (proCategory.equals("coming-soon")) {
            Pageable pageable = PageRequest.of(page, size);
            projectDtoPage = mainService.getAllProjectByComingSoon(pageable);
            List<ProjectDto> projectDtoList = projectDtoPage.getContent();
            sellerServiceImpl.proPaidCnt(projectDtoList);
            // 이미지 경로 설정
            projectDtoPage.forEach(projectDto -> {
                if (!projectDto.getProMainFilePaths().isEmpty()) {
                    projectDto.setMainPageImgPath(UPLOAD_PATH + projectDto.getProMainFilePaths().getFirst());
                } else {
                    projectDto.setMainPageImgPath("");
                }
            });
            return projectDtoPage;
        } else if (proCategory.equals("movie") || proCategory.equals("musical") || proCategory.equals("book")) {
            projectDtoPage = mainService.getAllProjectByProCategory(proCategory, PageRequest.of(page, size));
            List<ProjectDto> projectDtoList = projectDtoPage.getContent();
            sellerServiceImpl.proPaidCnt(projectDtoList);
            projectDtoPage.forEach(projectDto -> {
                if (!projectDto.getProMainFilePaths().isEmpty()) {
                    projectDto.setMainPageImgPath(UPLOAD_PATH + projectDto.getProMainFilePaths().getFirst());
                } else {
                    projectDto.setMainPageImgPath("");
                }
            });
            return projectDtoPage;
        }
        return projectDtoPage;
    }


    @GetMapping("/keyword")
    public Page<ProjectDto> getProjectsByProName(@RequestParam(value = "proName", defaultValue = "") String proName,
                                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                                 @RequestParam(name = "size", defaultValue = "12") int size,
                                                 Model model) {
        Page<ProjectDto> projectDtoPage = null;

        if (proName.equals("")) {
            projectDtoPage = mainService.findAllByOrderByProCode(PageRequest.of(page, size));
            List<ProjectDto> projectDtoList = projectDtoPage.getContent();
            sellerServiceImpl.proPaidCnt(projectDtoList);
            projectDtoPage.forEach(projectDto -> {
                if (!projectDto.getProMainFilePaths().isEmpty()) {
                    projectDto.setMainPageImgPath(UPLOAD_PATH + projectDto.getProMainFilePaths().getFirst());
                } else {
                    projectDto.setMainPageImgPath(""); // 이미지가 없는 경우 처리
                }
            });
            return projectDtoPage;


        } else {
            projectDtoPage = mainService.getProjectByProName(proName, PageRequest.of(page, size));
            List<ProjectDto> projectDtoList = projectDtoPage.getContent();
            sellerServiceImpl.proPaidCnt(projectDtoList);
            projectDtoPage.forEach(projectDto -> {
                if (!projectDto.getProMainFilePaths().isEmpty()) {
                    projectDto.setMainPageImgPath(UPLOAD_PATH + projectDto.getProMainFilePaths().getFirst());
                } else {
                    projectDto.setMainPageImgPath(""); // 이미지가 없는 경우 처리
                }
            });
            return projectDtoPage;
        }
    }

}