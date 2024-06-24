package com.example.app.controller.OrderController;

import com.example.app.domain.dto.OrderDto;
import com.example.app.domain.dto.OrderHistoryDto;
import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.repository.ProjectRepository;
import com.example.app.domain.service.order.OrderService;
import com.example.app.domain.service.project.ProjectServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/th/payment")
public class OrderDetailController {

    // 프로젝트 경로 (추후 변경 가능성 있음)
    String UPLOAD_PATH = "http://localhost:8080/upload/";

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectServiceImpl projectService;

//    @Value("${portOne.rest-api}")
//    private String portOne_API;
//    @Value("${portOne.secret}")
//    private String portOne_SECRET;

    // 주문 페이지 랜더링 API
    @GetMapping("/payment/{proCode}")
    String payment(@PathVariable("proCode") String proCode, Model model) throws Exception {
        log.info("payment() proCode : " + proCode); // 해당 프로젝트 proCode 확인
        Integer projectCode = Integer.parseInt(proCode); // proCode 'int' 형 변환
        Project project = projectRepository.findByProCode(projectCode); // 해당 proCode 의 project 엔터티 행 찾기 및 저장
        ProjectDto projectDto = ProjectDto.toProjectDto(project); // Entity -> Dto

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        // ProMainImg 를 리스트에 담기 (3개 고정)
        if (projectDto.getProCategory().equals("book")) {
            model.addAttribute("deliveryPay", "3000");
        } else {
            model.addAttribute("deliveryPay", "0");
        }
        List<String> storedFileName = projectDto.getStoredFileName();
        model.addAttribute("userId", userId);
        model.addAttribute("Project", projectDto);
        model.addAttribute("mainImg", UPLOAD_PATH + storedFileName.get(0));
        /*orderService.getToken();*/
        return "th/payment/payment";
    }

    // 주문 처리 API
    @PostMapping("/complete")
    public void paymentComplete(@RequestBody OrderDto orderDto) throws Exception {
        log.info("POST /th/payment/complete....");

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        orderDto.setUserId(userId);
        orderDto.setOrderState("결제완료");
        System.out.println("orderDto = " + orderDto);

        orderService.savePayment(orderDto);
    }

    //결제 상세 뷰 랜더링 API
    @GetMapping("/paymentHistory")
    public String showPaymentHistory(Model model) {
        log.info("GET /th/payment/paymentHistory...");

        List<OrderDto> orderDtoList = orderService.getPaymentHistory();
        List<String> proNameList = new ArrayList<>();
        List<String> imgPathList = new ArrayList<>();
        for (OrderDto orderDto : orderDtoList) {
            ProjectDto projectDto = projectService.findByProCode(orderDto.getProCode());
            System.out.println("projectDto = " + projectDto);

            String proName = projectDto.getProName();
            proNameList.add(proName);
            // proNameList.add(projectDto.getProName()); // 상단의 코드와 해당 코드 중 무엇이 좋은 코드인지
            String imgPath = UPLOAD_PATH+projectDto.getStoredFileName().getFirst();
            imgPathList.add(imgPath);
        }
        OrderHistoryDto orderHistoryDto = new OrderHistoryDto(orderDtoList, proNameList, imgPathList);
        System.out.println("orderHistoryDto = " + orderHistoryDto);
        model.addAttribute("orderHistoryDto", orderHistoryDto);

        return "th/payment/paymentHistory";
    }


}


