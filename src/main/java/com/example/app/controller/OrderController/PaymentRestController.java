package com.example.app.controller.OrderController;

import com.example.app.domain.dto.OrderDto;
import com.example.app.domain.dto.RefundDto;
import com.example.app.domain.service.order.OrderService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/api")
public class PaymentRestController {

    @Autowired
    private OrderService orderService;

    // 주문 처리 API
    @PostMapping("/complete")
    public ResponseEntity<String> paymentComplete(@RequestBody OrderDto orderDto) throws Exception {
        log.info("POST /th/payment/complete....");

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            orderDto.setUserId(userId);
            orderDto.setOrderState("결제완료");
            orderService.savePayment(orderDto);
            return ResponseEntity.ok("Payment request processed successfully for userId: " + userId);
        } catch (Exception e) {
            log.error("Failed to process payment complete request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process payment complete request");
        }

    }

    // 환불 처리 API
    @PostMapping("/refund")
    public ResponseEntity<String> refund(@RequestBody RefundDto refundDto) {
        log.info("POST /api/refund");
        System.out.println("refundDto = " + refundDto);
        try {
            orderService.cancelOrder(refundDto);
            return ResponseEntity.ok("Refund request processed successfully for imp_uid: " + refundDto.getImp_uid());
        } catch (Exception e) {
            log.error("Failed to process refund request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process refund request");
        }
    }


}
