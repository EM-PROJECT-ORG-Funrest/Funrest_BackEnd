package com.example.app.controller.OrderController;

import com.example.app.domain.dto.OrderDto;
import com.example.app.domain.repository.ProjectRepository;
import com.example.app.domain.service.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("/th/payment")
public class OrderDetailController {

    @Autowired
    private OrderService orderService;

    @Autowired
    ProjectRepository projectRepository;

//    @GetMapping("/payment/{proCode}")
//    String project(@PathVariable("proCode") String proCode, Model model) {
//        System.out.println(proCode); // 해당 프로젝트 proCode 확인
//        Integer projectCode = Integer.parseInt(proCode); // proCode 'int' 형 변환
//        Project project = projectRepository.findByProCode(projectCode); // 해당 proCode 의 project 엔터티 행 찾기 및 저장
//        ProjectDto projectDto = ProjectDto.toProjectDto(project); // Entity -> Dto

//        // ProMainImg 를 리스트에 담기 (3개 고정)
//        List<String> storedFileName = projectDto.getStoredFileName();
//        // ProMainImg 리스트 -> model 에 담기
//        model.addAttribute("Project", projectDto);
//        for (int i = 0; i < storedFileName.size(); i++) {
//            model.addAttribute("image"+(i+1), UPLOAD_PATH + storedFileName.get(i));
//        }
//
//        // ProSubImg 를 리스트에 담기 (5개 고정)
//        List<String> subStoredFileName = projectDto.getSubStoredFileName();
//        // ProSubImg 리스트 -> model 에 담기
//        for (int i = 0; i < subStoredFileName.size(); i++) {
//            model.addAttribute("subImage" + (i + 1), UPLOAD_PATH + subStoredFileName.get(i));
//        }
//        return "th/project/project.html";
//        return "th/payment/payment.html";
//    }


    // 주문 페이지로 이동(GET)
    @GetMapping("/payment")
    public String showPaymentForm() {
        return "th/payment/payment";
    }

    // 주문 처리(POST)
    @PostMapping("/payment")
    public String savePaymentInfo(@ModelAttribute OrderDto orderDto) {
        log.info("POST /th/payment/payment...." + orderDto);

        // 주문 정보 저장
        orderService.savePayment(orderDto);

        // 주문 성공 후 결제 상세 페이지로 리다이렉트
        return "redirect:/th/payment/paymentDetail";
    }

    // 결제 상세 페이지(GET)
    @GetMapping("/paymentDetail")
    public String showPaymentDetail() {
        log.info("GET /th/payment/paymentDetail...");
        return "th/payment/paymentDetail";
    }

}
