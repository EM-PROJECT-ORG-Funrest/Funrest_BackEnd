package com.example.app.controller.admin;

import com.example.app.domain.service.ProjectServiceImpl;
import com.example.app.domain.service.member.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/api")
public class AdminRestController {

    @Autowired
    UserService userService;

    @Autowired
    ProjectServiceImpl projectServiceImpl;

    @PostMapping("/deleteMembers")
    public ResponseEntity<String> deleteMembers(@RequestBody Map<String, List<String>> payload) {
        log.info("POST /api/deleteMembers");
        // 전달된 payload(JSON)의 userId 값 배열에 저장
        List<String> ids = payload.get("ids");
        // 회원삭제
        userService.deleteUsersByIds(ids);
        return ResponseEntity.ok("Deleted successfully");
    }

    @PostMapping("/deleteProjects")
    public ResponseEntity<String> deleteProjects(@RequestBody Map<String, List<Integer>> payload) {
        log.info("POST /api/deleteProjects");
        // 전달된 payload(JSON)의 proCode 값 배열에 저장
        List<Integer> proCodes = payload.get("proCodes");
        // 프로젝트 삭제
        projectServiceImpl.deleteProjectsByProCodes(proCodes);

        return ResponseEntity.ok("Deleted successfully");
    }
}
