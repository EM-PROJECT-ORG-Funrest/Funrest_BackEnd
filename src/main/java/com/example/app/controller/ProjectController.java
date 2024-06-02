package com.example.app.controller;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    //SELLER 페이지에서 개인 프로젝트 클릭 시 proCode를 input hidden값으로 전달해서 받아주는 연결페이지
    // 바로 redirect로 projectUpdate 페이지로 넘겨줌
    @GetMapping("/myPage/seller/projectUpdatecallBack")
    public String projectUpdatecallBack(@RequestParam("proCode") int proCode, HttpSession session){
       // projectRepository 에서 findBy 함수 사용해서 proCode로 projectEntity 겁색
        Project project = projectRepository.findByProCode(proCode);

        // 검색한 project 엔티티를 projectDto로 변환
        ProjectDto projectDto  = ProjectDto.ToDto(project);

        // projectDto를 HttpSession 활용 해서 전달 redirect한 페이지로 전달
        session.setAttribute("projectDto", projectDto);
        return "redirect:/th/myPage/seller/projectUpdate";
    }

    @GetMapping("/myPage/seller/projectUpdate")
    public String  projectUpdate(HttpSession session, Model model) throws ParseException {
        // session으로 전달된 객체를 여기서 받아옴
        ProjectDto projectDto = (ProjectDto) session.getAttribute("projectDto");

        //파싱작업
        projectServiceImpl.ParseDate(projectDto);

        //현재 페이지에서 값 표출하기위해 model 로 묶어서 전달 projectUpdate로 전달됨
        model.addAttribute("projectDto", projectDto);
        // 세션 낭비 방지를 위해 삭제 작업 진행
        session.removeAttribute("projectDto");

        return "th/myPage/seller/projectUpdate";
    }

    @GetMapping("/myPage/seller/projectCreateUpdateCallBack")
    public String  projectCreateUpdateCallBack(ProjectDto projectDto) {
        // projectUpdate 페이지에서 완료 버튼 누르면 update 작업 수행
        projectServiceImpl.UpdateProject(projectDto);
        // update 작업 수행 후 바로 redirect로 seller 페이지로 전달
        return "redirect:/th/myPage/seller/seller";
    }
}
