package com.example.app.controller.order;

import com.example.app.domain.dto.OrderDto;
import com.example.app.domain.dto.OrderHistoryDto;
import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
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

    // 이미지 파일 기본 경로
    private static final String UPLOAD_PATH = "https://funrestbucket.s3.ap-northeast-2.amazonaws.com/";

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectServiceImpl projectService;

    // 주문 페이지 랜더링 API
    @GetMapping("/payment/{proCode}")
    String payment(@PathVariable("proCode") String proCode, Model model) throws Exception {
        try {
            log.info("payment() proCode : " + proCode); // 해당 프로젝트 proCode 확인
            Integer projectCode = Integer.parseInt(proCode); // proCode 'int' 형 변환
            Project project = projectRepository.findByProCode(projectCode); // 해당 proCode 의 project 엔터티 행 찾기 및 저장
            ProjectDto projectDto = ProjectDto.toDto(project); // Entity -> Dto

            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            // ProMainImg 를 리스트에 담기 (3개 고정)
//            if (projectDto.getProCategory().equals("book")) {
//                model.addAttribute("deliveryPay", "3000");
//            } else {
//                model.addAttribute("deliveryPay", "0");
//            }
            int deliveryPay = 0; // 기본값 설정
            if (projectDto.getProCategory().equals("book")) {
                deliveryPay = 3000;
            }
            model.addAttribute("deliveryPay", deliveryPay);

            model.addAttribute("userId", userId);
            model.addAttribute("Project", projectDto);
            model.addAttribute("mainImg", UPLOAD_PATH + projectDto.getProMainFilePaths().getFirst());
            return "th/payment/payment";
        }catch (Exception e) {
            // 예외 발생 시 에러 페이지로 리다이렉트 또는 예외 처리
            model.addAttribute("error", "주문 페이지 랜더링 중 오류가 발생했습니다.");
            return "th/payment/error";
        }
    }

    // 반품 신청페이지 랜더링 API
    @GetMapping("/applyRefund/{impUid}")
    public String applyRefundOrder(@PathVariable("impUid") String impUid, Model model){
        log.info("GET /th/payment/applyRefund...");
        // 해당 orderCode 주문 정보 조회
        OrderDto orderDto = orderService.findById(impUid);
        model.addAttribute("order", orderDto);
        // 해당 주문의 proCode 조회
        ProjectDto projectDto = projectService.findByProCode(orderDto.getProCode());
        model.addAttribute("project", projectDto);
        // 해당 상품의 이미지 경로 조회
        model.addAttribute("imgPath", UPLOAD_PATH+projectDto.getProMainFilePaths().getFirst());

        return "th/payment/applyRefund.html";
    }

    // 결제 내역페이지 랜더링 API
    @GetMapping("/paymentHistory/{userId}")
    public String showPaymentHistory(@PathVariable("userId") User userId,
                                     @RequestParam("orderState") String orderState,
                                     Model model) {
        log.info("GET /th/payment/paymentHistory...");
        List<OrderDto> orderDtoList = orderService.findByUserIdAndOrderStateOrderByOrderDateDesc(userId, orderState);
        List<String> proNameList = new ArrayList<>();
        List<String> imgPathList = new ArrayList<>();
        for (OrderDto orderDto : orderDtoList) {
            ProjectDto projectDto = projectService.findByProCode(orderDto.getProCode());
            proNameList.add(projectDto.getProName());
            imgPathList.add(UPLOAD_PATH+projectDto.getProMainFilePaths().getFirst());
        }
        OrderHistoryDto orderHistoryDto = new OrderHistoryDto(orderDtoList, proNameList, imgPathList);
        model.addAttribute("orderHistoryDto", orderHistoryDto);

        return "th/payment/paymentHistory";
    }

    // 환불 내역페이지 랜더링 API
    @GetMapping("/refundHistory/{userId}")
    public String refundHistory(@PathVariable("userId") User userId,
                                @RequestParam("orderState") String orderState,
                                Model model) {
        log.info("GET /th/payment/refundHistory...");
        List<OrderDto> orderDtoList = orderService.findByUserIdAndOrderStateOrderByOrderDateDesc(userId, orderState);
        List<String> proNameList = new ArrayList<>();
        List<String> imgPathList = new ArrayList<>();
        for (OrderDto orderDto : orderDtoList) {
            ProjectDto projectDto = projectService.findByProCode(orderDto.getProCode());
            proNameList.add(projectDto.getProName());
            imgPathList.add(UPLOAD_PATH+projectDto.getProMainFilePaths().getFirst());
        }
        OrderHistoryDto orderHistoryDto = new OrderHistoryDto(orderDtoList, proNameList, imgPathList);
        model.addAttribute("orderHistoryDto", orderHistoryDto);

        return "th/payment/refundHistory";
    }

    // 결제 상세페이지 랜더링 API
    @GetMapping("/paymentDetail/{orderCode}")
    public String paymentDetail (@PathVariable("orderCode") String orderCode, Model model) {
        log.info("GET /th/payment/paymentDetail... orderCode: " + orderCode);
        // 해당 orderCode 주문 정보 조회
        OrderDto orderDto = orderService.findById(orderCode);
        model.addAttribute("order", orderDto);
        // 해당 주문의 proCode 조회
        ProjectDto projectDto = projectService.findByProCode(orderDto.getProCode());
        model.addAttribute("project", projectDto);
        // 해당 상품의 이미지 경로 조회
        String imgPath = UPLOAD_PATH+projectDto.getProMainFilePaths().getFirst();
        model.addAttribute("imgPath", imgPath);

        return "th/payment/paymentDetail.html";
    }


}


