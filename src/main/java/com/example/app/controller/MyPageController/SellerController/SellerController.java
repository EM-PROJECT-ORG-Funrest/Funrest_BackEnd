package com.example.app.controller.MyPageController.SellerController;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/th/myPage/seller")
public class SellerController {

    //SecurityContextHolder 에서 userId 가져요기
    String userId = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = new User();

    @GetMapping("/seller")
    public void seller() {
        log.info("GET /th/myPage/seller/seller .....");

        //userId 삽입
        user.setUserId(userId);
        //userId로 project 찾아야함
        //여기부턴 내일...
    }
}
