package com.example.app.controller.OrderController;

import com.example.app.controller.MyPageController.BuyerController.BuyerController;
import com.example.app.domain.dto.OrderDto;
import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Order;
import com.example.app.domain.entity.Project;
import com.example.app.domain.repository.ProjectRepository;
import com.example.app.domain.service.order.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import retrofit2.http.GET;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/th/payment")
public class OrderDetailController {

    // 프로젝트 경로 (추후 변경 가능성 있음)
    private final String UPLOAD_PATH = "http://localhost:8080/upload/";

    @Autowired
    private OrderService orderService;
    @Autowired
    ProjectRepository projectRepository;

    @GetMapping("/payment/{proCode}")
    String payment(@PathVariable("proCode") String proCode, Model model) {
        log.info("payment() proCode : " + proCode); // 해당 프로젝트 proCode 확인
        Integer projectCode = Integer.parseInt(proCode); // proCode 'int' 형 변환
        Project project = projectRepository.findByProCode(projectCode); // 해당 proCode 의 project 엔터티 행 찾기 및 저장
        ProjectDto projectDto = ProjectDto.toProjectDto(project); // Entity -> Dto

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        // ProMainImg 를 리스트에 담기 (3개 고정)
        if (projectDto.getProCategory().equals("book")){
            model.addAttribute("deliveryPay","3000" );
        }else{
            model.addAttribute("deliveryPay","0" );
        }
        List<String> storedFileName = projectDto.getStoredFileName();
        model.addAttribute("userId", userId);
        model.addAttribute("Project", projectDto);
        model.addAttribute("mainImg", UPLOAD_PATH + storedFileName.get(0));

        return "th/payment/payment";
    }

//    // 주문 처리(POST)
//    @PostMapping("/payment/complete")
//    public String savePaymentInfo(OrderDto orderDto) {
//        log.info("POST /th/payment/payment...." + orderDto);
//
//        // 여기까지되는데 orderDto안에 안들어 값 DB에 저장될값
//        //USERiD
//
//        // 주문 정보 저장
//        orderService.savePayment(orderDto);
//
//        // 주문 성공 후 결제 상세 페이지로 리다이렉트
//
//        return "redirect:/th/main/main";
//    }

    // 주문 처리(POST)
    @PostMapping("/complete")

    public @ResponseBody void paymentComplete(@RequestBody OrderDto orderDto){
        log.info("POST /th/payment/complete...." + orderDto);

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        orderDto.setUserId(userId);
        orderDto.setOrderState("결제완료");

        System.out.println("orderDto..!!!!!"+orderDto);

        orderService.savePayment(orderDto);
    }

     //결제 상세 페이지(GET)
    @GetMapping("/paymentHistory")
    public void  showPaymentHistory() {
        log.info("GET /th/payment/paymentHistory...");
    }
}





