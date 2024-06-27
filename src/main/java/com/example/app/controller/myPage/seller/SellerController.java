package com.example.app.controller.myPage.seller;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.User;
import com.example.app.domain.service.myPage.SellerServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/th/myPage/seller")
public class SellerController {

    // 프로젝트 경로 (추후 변경 가능성 있음)
    private static final String UPLOAD_PATH = "http://3.39.29.162:8080/upload/";

    @Autowired
    SellerServiceImpl sellerServiceImpl;


    @GetMapping("/seller")
    public String seller(Model model) {
        log.info("GET /th/myPage/seller/seller .....");

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = new User();
        user.setUserId(userId);

        // user 별 project 찾기
        List<ProjectDto> projectDtoList = sellerServiceImpl.findProjectByUserId(user);
        // user 별 생성 프로젝트 갯수 찾기
        Long countProject = sellerServiceImpl.countProjectByUserId(user);
        // 사용자 이름 넣어주기
        User user1 = sellerServiceImpl.findUserByUserId(userId);
        String userName = user1.getUserName();
        //사진 넣기
        for (int i = 0; i < projectDtoList.size(); i++) {
            projectDtoList.get(i).setMainPageImgPath(UPLOAD_PATH + projectDtoList.get(i).getStoredFileName().get(0));
            System.out.println("projectDtoList.get(i).getProCode()" + projectDtoList.get(i).getProCode());
        }

        // 신청 날짜 보여주기 포매팅
        sellerServiceImpl.formattedDate(projectDtoList);
        // 프로젝트 기간 보여주기 datetime 에 저장
        sellerServiceImpl.dateTime(projectDtoList);
        // 프로젝트 참여인원 가져오기
        sellerServiceImpl.proPaidCnt(projectDtoList);
        // 달성률 가져오기
        sellerServiceImpl.proAchievementRate(projectDtoList);

        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        model.addAttribute("projectDtoList", projectDtoList);
        model.addAttribute("countProject", countProject);

        return "th/myPage/seller/seller";
    }
}
