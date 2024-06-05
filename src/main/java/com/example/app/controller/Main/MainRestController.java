package com.example.app.controller.Main;



import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.service.main.MainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
        //List<Project> findAllByProCategoryOrderByProCode(String proCategory); 이거 사용해서 한번에 다 보내보자~~ 다받아오고
//        List<ProjectDto> projectDtos = projectDtoPage.stream().toList();
//        model.addAttribute("ProjectDto", projectDtos);

        return projectDtoPage;

    }


    @GetMapping("/category")
    public Page<ProjectDto> getProjectsByCategory(@RequestParam(value = "proCategory", defaultValue = "all") String proCategory,
                                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "12") int size,
                                                  Model model) {
        Page<ProjectDto> projectDtoPage = null;
        // 카테고리 들어오는 값 별로 나누기
        if (proCategory.equals("all")) {
            // List를 Page로 변환 ProjectRepository에서 Page<projectDto> findAll(); 하니까 List형태랑 겹친다 해서 그냥 List로 받고 변환 해줌
            List<ProjectDto> projectDtoList =  mainService.getAllProject();
            projectDtoPage = mainService.ListToPage(page, size, projectDtoList);
            return projectDtoPage;

        } else if (proCategory.equals("movie")){
            projectDtoPage = mainService.getAllProjectByProCategory(proCategory,PageRequest.of(page, size));
            return projectDtoPage;

        }else if (proCategory.equals("musical")){
            projectDtoPage = mainService.getAllProjectByProCategory(proCategory,PageRequest.of(page, size));
            return projectDtoPage;

        }else if (proCategory.equals("book")){
            projectDtoPage = mainService.getAllProjectByProCategory(proCategory,PageRequest.of(page, size));
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

        if (proName == null && proName.equals("")){
            List<ProjectDto> projectDtoList =  mainService.getAllProject();
            projectDtoPage = mainService.ListToPage(page, size, projectDtoList);
            return projectDtoPage;
        }else{
            System.out.println(proName);
            projectDtoPage = mainService.getProjectByProName(proName, PageRequest.of(page, size));
            return projectDtoPage;
        }
    }
}