package com.example.app.controller.admin;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.dto.UserDto;
import com.example.app.domain.service.ProjectServiceImpl;
import com.example.app.domain.service.member.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/th/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    ProjectServiceImpl projectServiceImpl;

    @GetMapping("/dashboard")
    public void adminDashboard(Model model) {
        log.info("GET /th/admin/dashboard");
        // 관리자 계정 전달
        String adminId = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("adminId",adminId);
    }

    @GetMapping("/member")
    public void adminMember(@RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "size", defaultValue = "2") int size,
                            Model model) {
        log.info("GET /th/admin/member");
        // 1. 전체 회원 정보 조회 (페이징 처리)
        Page<UserDto> userDtoPage = userService.getAllUsers(PageRequest.of(page, size));
        // 2. 회원 관리 게시판 페이지 버튼
        int startPage = Math.max(1, userDtoPage.getPageable().getPageNumber() -4);
        int endPage = Math.min(userDtoPage.getPageable().getPageNumber() + 4, userDtoPage.getTotalPages());
        // 3. 페이지를 넘겨도 번호를 계속 증가하기 위한 현재 페이지 번호 및 페이지 당 항목 수 저장
        int currentPage = userDtoPage.getPageable().getPageNumber(); // 0 부터 시작
        int pageSize = size;
        // 3. 모델 속성에 정보 저장
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("userDtoPages", userDtoPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        // 4. 관리자 계정 전달
        String adminId = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("adminId",adminId);
    }

    @GetMapping("/project")
    public void adminProject(@RequestParam(name = "unapprovedPage", defaultValue = "0") int unapprovedPage,
                             @RequestParam(name = "approvedPage", defaultValue = "0") int approvedPage,
                             @RequestParam(name = "size", defaultValue = "10") int size,
                             Model model) {
        log.info("GET /th/admin/project");
        // 1. 미승인 프로젝트 정보 조회 (페이징 처리)
        Page<ProjectDto> projectDtoPageBeforeList = projectServiceImpl.findByProStatus(0, PageRequest.of(unapprovedPage, size));
        // 2. 승인된 프로젝트 정보 조회 (페이징 처리)
        Page<ProjectDto> projectDtoPageAfterList = projectServiceImpl.findByProStatus(1, PageRequest.of(approvedPage, size));
        // 3. 미승인 프로젝트 게시판 페이지 버튼
        int startPage = Math.max(1, projectDtoPageBeforeList.getPageable().getPageNumber() -4);
        int endPage = Math.min(projectDtoPageBeforeList.getPageable().getPageNumber() + 4, projectDtoPageBeforeList.getTotalPages());
        // 4. 승인 프로젝트 게시판 페이지 버튼
        int permitStartPage = Math.max(1, projectDtoPageAfterList.getPageable().getPageNumber() -4);
        int permitEndPage = Math.min(projectDtoPageAfterList.getPageable().getPageNumber() + 4, projectDtoPageAfterList.getTotalPages());
        // 5. 페이지를 넘겨도 번호를 계속 증가하기 위한 현재 페이지 번호 및 페이지 당 항목 수 저장
        int beforeCurrentPage = projectDtoPageBeforeList.getPageable().getPageNumber(); // 0 부터 시작
        int afterCurrentPage = projectDtoPageAfterList.getPageable().getPageNumber();
        int pageSize = size;
        // 6. 모델 속성에 정보 저장
        model.addAttribute("beforeCurrentPage", beforeCurrentPage);
        model.addAttribute("afterCurrentPage", afterCurrentPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("projectBeforeList", projectDtoPageBeforeList);
        model.addAttribute("permitStartPage", permitStartPage);
        model.addAttribute("permitEndPage", permitEndPage);
        model.addAttribute("projectAfterList", projectDtoPageAfterList);
        // 4. 관리자 계정 전달
        String adminId = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("adminId",adminId);
    }

    @GetMapping("/editProject")
    public void adminEditProject(@RequestParam("proCode") String proCode, Model model) {
        log.info("GET /th/admin/editProject/{}", proCode);
        Integer projectCode = Integer.parseInt(proCode);
        // 1. 수정할 프로젝트 정보 조회
        ProjectDto projectDto = projectServiceImpl.findByProCode(projectCode);
        System.out.println("projectDto = " + projectDto);
        // 2. 모델 속성에 정보 저장
        model.addAttribute("project", projectDto);
    }


}
