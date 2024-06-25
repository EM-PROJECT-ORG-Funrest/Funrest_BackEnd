package com.example.app.controller.OrderController;

import com.example.app.domain.dto.RefundDto;
import com.example.app.domain.service.order.OrderService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // 반품 신청 API
    @PostMapping("/refund")
    public ResponseEntity<String> refund(@RequestBody RefundDto refundDto) {
        log.info("POST /api/refund");

        String imp_uid = refundDto.getImp_uid();
        String reason = refundDto.getReason();
        try {
            // orderService 를 통해 환불 처리 로직을 호출
            orderService.cancelOrder(imp_uid, reason);
            return ResponseEntity.ok("Refund request processed successfully for imp_uid: " + imp_uid);
        } catch (Exception e) {
            log.error("Failed to process refund request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process refund request");
        }
    }


}
