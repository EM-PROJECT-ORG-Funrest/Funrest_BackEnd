package com.example.app.controller.admin;

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

    @PostMapping("/deleteMembers")
    public ResponseEntity<String> deleteMembers(@RequestBody Map<String, List<String>> payload) {
        log.info("POST /api/deleteMembers");

        List<String> ids = payload.get("ids");
        System.out.println("ids = " + ids);

        userService.deleteUsersByIds(ids);

        return ResponseEntity.ok("Deleted successfully");
    }
}
