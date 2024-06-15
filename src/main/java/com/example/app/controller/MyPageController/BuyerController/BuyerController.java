package com.example.app.controller.MyPageController.BuyerController;

import com.example.app.domain.entity.User;
import com.example.app.domain.repository.UserRepository;
import com.example.app.domain.service.ProjectServiceImpl;
import com.example.app.domain.service.myPage.BuyerServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/th/myPage/buyer")
public class BuyerController {

    @Autowired
    BuyerServiceImpl buyerService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/buyer")
    public String seller(Model model) {
        //SecurityContextHolder 에서 userId 가져요기
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user1 = new User();
        user1.setUserId(userId);

        // 결제 횟수 및 알림 신청 횟수 가져오기
        Long OrderCnt = buyerService.countOrderByUserId(user1);
        Long NotifyCnt = buyerService.countNotifyByUserId(user1);

        // 사용자 이름 넣어주기
        User user = buyerService.findUserByUserId(userId);
        String userName = user.getUserName();


        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        model.addAttribute("OrderCnt",OrderCnt);
        model.addAttribute("NotifyCnt",NotifyCnt);
        return "th/myPage/buyer/buyer";
    }

    @GetMapping("/notify")
    public void showNotify() {
        log.info("get /th/myPage/buyer/notify showNotify()");

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            buyerService.getAllNotifyByUserId(userId);
        } catch(RuntimeException e) {
            //ResponseEntity("Can not found User", HttpStatus.BAD_REQUEST);
        }


    }
}
