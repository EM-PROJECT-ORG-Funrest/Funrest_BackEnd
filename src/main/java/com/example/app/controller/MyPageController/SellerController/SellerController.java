package com.example.app.controller.MyPageController.SellerController;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
import com.example.app.domain.service.ProjectServiceImpl;
import com.example.app.domain.service.myPage.SellerServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/th/myPage/seller")
public class SellerController {

    // 프로젝트 경로 (추후 변경 가능성 있음)
    private static final String UPLOAD_PATH = "http://localhost:8080/upload/";

    @Autowired
    SellerServiceImpl sellerService;


    @GetMapping("/seller")
    public String seller(Model model) {
        //SecurityContextHolder 에서 userId 가져요기
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = new User();
        user.setUserId(userId);
        log.info("GET /th/myPage/seller/seller .....");
        //user 별 project 찾기
        List<ProjectDto> projectDtoList = sellerService.findProjectByUserId(user);
        //user 별 생성 프로젝트 갯수 찾기
        Long countProject= sellerService.countProjectByUserId(user);

        // 사용자 이름 넣어주기
        User user1 = sellerService.findUserByUserId(userId);
        String userName = user1.getUserName();

        //사진 넣기
        for (int i = 0; i < projectDtoList.size() ; i++) {
            projectDtoList.get(i).setMainPageImgPath(UPLOAD_PATH+projectDtoList.get(i).getStoredFileName().getFirst());
        }
        
        //달성률 구하기
        for (ProjectDto projectDto : projectDtoList) {
            // 문자열을 정수로 변환
            int proGoalAmount = Integer.parseInt(projectDto.getProGoalAmount().replace(",", ""));
            int proPrice = Integer.parseInt(projectDto.getProPrice().replace(",", ""));
            int proPaidCnt = projectDto.getProPaidCnt();

            // 달성률 계산
            int proAchievementRate = (proPrice * proPaidCnt / proGoalAmount) * 100;

            // 달성률 dto에 넣기
            projectDto.setProAchievementRate(proAchievementRate);
        }

        // 신청 날짜 보여주기 포매팅
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (ProjectDto projectDto : projectDtoList) {
            try {
                Date proDate = projectDto.getProDate();
                String formattedDate = outputFormat.format(proDate);
                projectDto.setFormattedProDate(formattedDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 프로젝트 기간 보여주기 datetime 에 저장
        for (ProjectDto projectDto : projectDtoList) {

            String proStartDate = projectDto.getProStartDate();
            String proEndDate = projectDto.getProEndDate();

            String dateTime = proStartDate + " ~ " + proEndDate;
            projectDto.setProDateTime(dateTime);

        }


        model.addAttribute("userName", userName);
        model.addAttribute("projectDtoList", projectDtoList);
        model.addAttribute("countProject", countProject);

        return "th/myPage/seller/seller";
    }
}
