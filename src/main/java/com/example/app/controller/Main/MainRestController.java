package com.example.app.controller.Main;


//import com.example.app.domain.dto.ProjectDto;
//import com.example.app.domain.service.main.MainServiceImpl;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//public class MainRestController {
//
//    private final MainServiceImpl mainService;
//
//    @Autowired
//    public MainRestController(MainServiceImpl mainService) {
//        this.mainService = mainService;
//    }
//
//    @GetMapping("/th/main/main/projects")
//    public Page<ProjectDto> getProjects(@RequestParam(defaultValue = "0") int page,
//                                        @RequestParam(defaultValue = "12") int size,
//                                        Model model) {
//        // 프로젝트를 페이지별로 검색하여 반환
//        Page<ProjectDto> projectDtoPage =  mainService.getAllProjectsOrderedByProCode(PageRequest.of(page, size));
//        List<ProjectDto> projectDto = projectDtoPage.getContent();
//        model.addAttribute("projectDtos", projectDto);
//        System.out.println(projectDto);
//
//        return projectDtoPage;
//    }
//}

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.service.main.MainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/th/main/main")
public class MainRestController {

    private final MainServiceImpl mainService;

    @Autowired
    public MainRestController(MainServiceImpl mainService) {
        this.mainService = mainService;
    }

    //무한 스크롤 mapping
    @GetMapping("/projects")
    public Page<ProjectDto> getProjects(@RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "size", defaultValue = "12") int size,
                                        Model model) {
        // 프로젝트를 페이지별로 검색하여 반환
        Page<ProjectDto> projectDtoPage =  mainService.getAllProjectsOrderedByProCode(PageRequest.of(page, size));
//        List<ProjectDto> projectDtos = projectDtoPage.stream().toList();
//        model.addAttribute("ProjectDto", projectDtos);

        return projectDtoPage;

    }


    @GetMapping("/category")
    public List<ProjectDto> getProjectsByCategory(@RequestParam("proCategory") String proCategory) {
        List<ProjectDto> projectDtos = null;
        if (proCategory.equals("all")) {
             projectDtos =  mainService.getAllProject();
            return projectDtos;
        } else if (proCategory.equals("movie")){
             projectDtos = mainService.getAllProjectByProCategory(proCategory);
            return projectDtos;
        }else if (proCategory.equals("musical")){
             projectDtos = mainService.getAllProjectByProCategory(proCategory);
            return projectDtos;
        }else if (proCategory.equals("book")){
             projectDtos = mainService.getAllProjectByProCategory(proCategory);
            return projectDtos;
        }

        return projectDtos;
    }


//    //프로젝트 키워드별 검색
//    @GetMapping("/findProjects")
//    public Page<ProjectDto> getKeywordProject(Model model,
//                                              @RequestParam(name="proName", defaultValue = "") String proName,
//                                              @RequestParam(name = "page", defaultValue = "0") int page,
//                                              @RequestParam(name = "size", defaultValue = "12") int size){
//        Page<ProjectDto> projectDtoPage = mainService.getAllProjectsByProName(proName,PageRequest.of(page, size));
//        List<ProjectDto> projectDtos = projectDtoPage.stream().toList();
//        model.addAttribute("ProjectDto", projectDtos);
//
//        return projectDtoPage;
//    }


}