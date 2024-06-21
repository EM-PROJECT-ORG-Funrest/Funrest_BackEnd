package com.example.app.controller.OrderController;

import com.example.app.controller.MyPageController.BuyerController.BuyerController;
import com.example.app.domain.dto.OrderDto;
import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Order;
import com.example.app.domain.entity.Project;
import com.example.app.domain.repository.ProjectRepository;
import com.example.app.domain.service.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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

//    @Value("${portOne.rest-api}")
//    private String portOne_API;
//    @Value("${portOne.secret}")
//    private String portOne_SECRET;


    @GetMapping("/payment/{proCode}")
    String payment(@PathVariable("proCode") String proCode, Model model) throws Exception {
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
        orderService.getToken();
        return "th/payment/payment";
    }


    // 주문 처리(POST)
    @PostMapping("/complete")
    public @ResponseBody void paymentComplete(@RequestBody OrderDto orderDto) throws Exception {


        log.info("POST /th/payment/complete...." + orderDto);

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        orderDto.setUserId(userId);
        orderDto.setOrderState("결제완료");

        System.out.println("orderDto..!!!!!"+orderDto);

        orderService.savePayment(orderDto);
    }

     //결제 상세 페이지(GET)
    @GetMapping("/paymentHistory")
    public String showPaymentHistory(Model model) {
        log.info("GET /th/payment/paymentHistory...");

        List<Order> paymentHistory = orderService.getPaymentHistory();
        System.out.println("paymentHistory : "+paymentHistory);

        model.addAttribute("paymentHistory",paymentHistory);

        return "th/payment/paymentHistory";
    }


}


