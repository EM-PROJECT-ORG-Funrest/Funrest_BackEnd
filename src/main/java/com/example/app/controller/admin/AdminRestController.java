package com.example.app.controller.admin;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.dto.VisitDto;
import com.example.app.domain.service.visit.VisitService;
import com.example.app.domain.service.member.UserService;
import com.example.app.domain.service.project.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/api")
public class AdminRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectServiceImpl projectServiceImpl;

    @Autowired
    private VisitService visitService;

    // 회원 삭제 API
    @PostMapping("/deleteMembers")
    public ResponseEntity<String> deleteMembers(@RequestBody Map<String, List<String>> payload) {
        log.info("POST /api/deleteMembers");
        // 전달된 payload(JSON)의 userId 값 배열에 저장
        List<String> ids = payload.get("ids");
        // 회원삭제
        userService.deleteUsersByIds(ids);
        return ResponseEntity.ok("Deleted successfully");
    }

    // 프로젝트 삭제 API
    @PostMapping("/deleteProjects")
    public ResponseEntity<String> deleteProjects(@RequestBody Map<String, List<Integer>> payload) {
        log.info("POST /api/deleteProjects");
        // 전달된 payload(JSON)의 proCode 값 배열에 저장
        List<Integer> proCodes = payload.get("proCodes");
        // 프로젝트 삭제
        projectServiceImpl.deleteProjectsByProCodes(proCodes);

        return ResponseEntity.ok("Deleted successfully");
    }

    // 프로젝트 수정 API
    @PostMapping("/editProject")
    public ModelAndView editProject(@ModelAttribute ProjectDto projectDto) throws IOException {
        log.info("POST /api/editProject");
        System.out.println("AdminRestController's ProjectDto : " + projectDto);
        Integer proCode = projectDto.getProCode();
        // 프로젝트 수정
        projectServiceImpl.updateProjectByProCode(proCode, projectDto);

        return new ModelAndView("redirect:/th/admin/project");
    }

    // 프로젝트 승인 API
    @PostMapping("/permitProjects")
    public ResponseEntity<String> permitProjects(@RequestBody Map<String, List<Integer>> payload) {
        log.info("POST /api/permitProjects");
        // 전달된 payload(JSON)의 proCode 값 배열에 저장
        List<Integer> proCodes = payload.get("proCodes");
        // 프로젝트 승인
        projectServiceImpl.permitProjectsByProCodes(proCodes);

        return ResponseEntity.ok("Permitted successfully");
    }

    // 승인 프로젝트 삭제 API
    @PostMapping("/permitDeleteProjects")
    public ResponseEntity<String> permitDeleteProjects(@RequestBody Map<String, List<Integer>> payload) {
        log.info("POST /api/permitDeleteProjects");
        // 전달된 payload(JSON)의 proCode 값 배열에 저장
        List<Integer> proCodes = payload.get("proCodes");
        // 프로젝트 삭제
        projectServiceImpl.deleteProjectsByProCodes(proCodes);

        return ResponseEntity.ok("Deleted successfully");
    }

    // 프로젝트 승인 취소 API
    @PostMapping("/permitCancelProjects")
    public ResponseEntity<String> permitCancelProjects(@RequestBody Map<String, List<Integer>> payload) {
        log.info("POST /api/permitCancelProjects");
        // 전달된 payload(JSON)의 proCode 값 배열에 저장
        List<Integer> proCodes = payload.get("proCodes");
        // 프로젝트 승인 취소
        projectServiceImpl.cancelProjectsByProCodes(proCodes);

        return ResponseEntity.ok("Permit Cancel successfully");
    }

    // 차트 데이터 전송 API
    @GetMapping("/visitData")
    public List<VisitDto> getVisitData() {
        return visitService.getVisitData();
    }

}
