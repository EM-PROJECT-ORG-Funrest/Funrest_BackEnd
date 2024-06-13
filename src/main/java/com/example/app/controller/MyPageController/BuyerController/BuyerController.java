package com.example.app.controller.MyPageController.BuyerController;

import com.example.app.domain.entity.User;
import com.example.app.domain.service.ProjectServiceImpl;
import com.example.app.domain.service.myPage.BuyerServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/th/myPage/buyer")
public class BuyerController {

    @Autowired
    BuyerServiceImpl buyerService;

    @GetMapping("/seller")
    public String seller(Model model) {
        //SecurityContextHolder 에서 userId 가져요기
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();


        // 결제 횟수 및 알림 신청 횟수 가져오기
        Long OrderCnt = buyerService.CountOrderByUserId(userId);
        Long NotifyCnt = buyerService.CountNotifyByUserId(userId);

        // 사용자 이름 넣어주기
        User user = buyerService.findUserByUserId(userId);

        model.addAttribute("user", user);
        model.addAttribute("OrderCnt",OrderCnt);
        model.addAttribute("NotifyCnt",NotifyCnt);
        return "th/myPage/buyer/buyer";
    }
}
