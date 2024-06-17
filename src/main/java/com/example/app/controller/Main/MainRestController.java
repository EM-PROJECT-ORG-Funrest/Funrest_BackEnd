package com.example.app.controller.Main;



import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.service.main.MainServiceImpl;
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

    private final MainServiceImpl mainService;

    // 프로젝트 경로 (추후 변경 가능성 있음)
    String UPLOAD_PATH = "http://localhost:8080/upload/";

    @Autowired
    public MainRestController(MainServiceImpl mainService) {
        this.mainService = mainService;
    }


    @GetMapping("/projects")
    public Page<ProjectDto> getProjects(@RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "size", defaultValue = "12") int size) {
        // 프로젝트를 페이지별로 검색하여 반환
        Page<ProjectDto> projectDtoPage = mainService.getAllProjectsOrderedByProCode(PageRequest.of(page, size));
        // 각 ProjectDto에 이미지 URL을 설정
        projectDtoPage.forEach(projectDto -> {
            if (!projectDto.getStoredFileName().isEmpty()) {
                projectDto.setMainPageImgPath(UPLOAD_PATH + projectDto.getStoredFileName().get(0));
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
        if (proCategory.equals("all")){
            //Pageable pageable = PageRequest.of(page, size, Sort.by("proCode").descending());
            // 프로젝트를 페이지별로 검색하여 반환
            //projectDtoPage = mainService.getAllProjectsOrderedByProCode(pageable);
            projectDtoPage = mainService.getAllProjectsOrderedByProCode(PageRequest.of(page, size));
            System.out.println("all : " + projectDtoPage.getContent());
            // 각 ProjectDto에 이미지 URL을 설정
            projectDtoPage.forEach(projectDto -> {
                if (!projectDto.getStoredFileName().isEmpty()) {
                    projectDto.setMainPageImgPath(UPLOAD_PATH + projectDto.getStoredFileName().get(0));
                } else {
                    projectDto.setMainPageImgPath(""); // 이미지가 없는 경우 처리
                }
            });

            return projectDtoPage;
        }else if (proCategory.equals("movie")){
            projectDtoPage = mainService.getAllProjectByProCategory(proCategory,PageRequest.of(page, size));
            System.out.println("movie : " + projectDtoPage.getContent());
            projectDtoPage.forEach(projectDto -> {
                if (!projectDto.getStoredFileName().isEmpty()) {
                    projectDto.setMainPageImgPath(UPLOAD_PATH + projectDto.getStoredFileName().getFirst());
                } else {
                    projectDto.setMainPageImgPath("");
                }
            });
            return projectDtoPage;

        }else if (proCategory.equals("musical")){
            projectDtoPage = mainService.getAllProjectByProCategory(proCategory,PageRequest.of(page, size));
            projectDtoPage.forEach(projectDto -> {
                if (!projectDto.getStoredFileName().isEmpty()) {
                    projectDto.setMainPageImgPath(UPLOAD_PATH + projectDto.getStoredFileName().getFirst());
                } else {
                    projectDto.setMainPageImgPath("");
                }
            });
            return projectDtoPage;

        }else if (proCategory.equals("book")){
            projectDtoPage = mainService.getAllProjectByProCategory(proCategory,PageRequest.of(page, size));
            projectDtoPage.forEach(projectDto -> {
                if (!projectDto.getStoredFileName().isEmpty()) {
                    projectDto.setMainPageImgPath(UPLOAD_PATH + projectDto.getStoredFileName().getFirst());
                } else {
                    projectDto.setMainPageImgPath("");
                }
            });
            return projectDtoPage;

        }

        return projectDtoPage;
    }



    @GetMapping("/keyword")
    public Page<ProjectDto> getProjectsByProName(@RequestParam(value = "proName", defaultValue = "1234578888") String proName,
                                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                                 @RequestParam(name = "size", defaultValue = "12") int size,
                                                 Model model) {
        Page<ProjectDto> projectDtoPage = null;

        if (proName.equals("1234578888")) {
            projectDtoPage =  mainService.findAllByOrderByProCode(PageRequest.of(page, size));
            log.info("1234578888" + projectDtoPage.getContent().getFirst());
            projectDtoPage.forEach(projectDto -> {
                if (!projectDto.getStoredFileName().isEmpty()) {
                    projectDto.setMainPageImgPath(UPLOAD_PATH + projectDto.getStoredFileName().getFirst());
                } else {
                    projectDto.setMainPageImgPath(""); // 이미지가 없는 경우 처리
                }
            });
            return projectDtoPage;
        }else{
            projectDtoPage = mainService.getProjectByProName(proName, PageRequest.of(page, size));
            projectDtoPage.forEach(projectDto -> {
                if (!projectDto.getStoredFileName().isEmpty()) {
                    projectDto.setMainPageImgPath(UPLOAD_PATH + projectDto.getStoredFileName().getFirst());
                } else {
                    projectDto.setMainPageImgPath(""); // 이미지가 없는 경우 처리
                }
            });
            return projectDtoPage;
        }
    }
}