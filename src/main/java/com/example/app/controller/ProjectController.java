package com.example.app.controller;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.repository.ProjectRepository;
import com.example.app.domain.service.member.ProjectServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/th")
public class ProjectController {

    // 프로젝트 관련 비즈니스 로직을 구현할 객체 주입
    @Autowired
    private ProjectServiceImpl projectServiceImpl;

    @Autowired
    private ProjectRepository projectRepository;

    //@Autowired
    //private FileUpload fileUpload;

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

        return "redirect:/th/myPage/seller/seller";
    }

    @GetMapping("/myPage/seller/projectUpdatecallBack")
    public String projectUpdatecallBack(@RequestParam("proCode") int proCode, HttpSession session){
        System.out.println(proCode);
        Project project = projectRepository.findByProCode(proCode);
        System.out.println(project);
        ProjectDto projectDto  = ProjectDto.ToDto(project);
        System.out.println(projectDto);
        session.setAttribute("projectDto", projectDto);
        return "redirect:/th/myPage/seller/projectUpdate";
    }

    @GetMapping("/myPage/seller/projectUpdate")
    public String  projectUpdate(HttpSession session, Model model) {
        ProjectDto projectDto = (ProjectDto) session.getAttribute("projectDto");
        System.out.println("/th/myPage/seller/projectUpdate invoke... projectDto :" + projectDto);
        String datetimes = projectDto.getProStartDate() + "-" + projectDto.getProEndDate();
        projectDto.setDatetimes(datetimes);
        model.addAttribute("projectDto", projectDto);
        session.removeAttribute("projectDto"); // 세션 낭비 방지

        return "th/myPage/seller/projectUpdate";
    }

    @GetMapping("/th/myPage/seller/projectCreateUpdateCallBack")
    public String  projectCreateUpdateCallBack( ProjectDto projectDto) {
        System.out.println("projectCreateUpdateCallBack projectDto" + projectDto);
        projectServiceImpl.UpdateProject(projectDto);
        System.out.println("dsasaddasadadsadsadsadsadasd");
        return "redirect:/th/myPage/seller/seller";
    }
}
