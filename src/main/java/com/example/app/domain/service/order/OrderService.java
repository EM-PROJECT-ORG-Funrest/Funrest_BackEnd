package com.example.app.domain.service.order;

import com.example.app.domain.dto.OrderDto;
import com.example.app.domain.dto.RefundDto;
import com.example.app.domain.entity.Order;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
import com.example.app.domain.repository.OrderRepository;
import com.example.app.domain.repository.ProjectRepository;
import com.example.app.domain.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${portOne.rest-api}")
    private String portOne_API;
    @Value("${portOne.secret}")
    private String portOne_SECRET;

    private PortOneTokenResponse portOneTokenResponse;
    private PortOneAuthInfoResponse portOneAuthInfoResponse;

    public @ResponseBody void getToken() throws Exception {

        //URL
        String url = "https://api.iamport.kr/users/getToken";

        //HEADER
        HttpHeaders headers = new HttpHeaders();

        //PARAMS
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("imp_key", portOne_API);
        params.add("imp_secret", portOne_SECRET);

        //ENTITY
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        //REQUEST
        RestTemplate rt = new RestTemplate();
        ResponseEntity<PortOneTokenResponse> response = rt.exchange(url, HttpMethod.POST, entity, PortOneTokenResponse.class);

        //RESPONSE
        System.out.println("response.getBody() + token" + response.getBody());
        this.portOneTokenResponse = response.getBody();
    }

    @GetMapping(value = "getBuyerInfo/{imp_uid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody PortOneAuthInfoResponse getBuyerInfo(@PathVariable("imp_uid") String imp_uid) throws Exception {
        getToken();
        log.info("GET /getBuyerInfo..." + imp_uid);

        //URL
        String url = "https://api.iamport.kr/certifications/" + imp_uid;

        //HEADER
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + portOneTokenResponse.getResponse().getAccess_token());
        //PARAMS
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        //ENTITY
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        //REQUEST
        RestTemplate rt = new RestTemplate();
        ResponseEntity<PortOneAuthInfoResponse> response = rt.exchange(url, HttpMethod.GET, entity, PortOneAuthInfoResponse.class);

        //RESPONSE
        System.out.println(response.getBody());

        return response.getBody();
    }

    // 결제 처리
    @Transactional
    public void savePayment(OrderDto orderDto) {
        Project project = projectRepository.findByProCode(orderDto.getProCode());
        System.out.println("project = " + project);
        Optional<User> optionalUser = userRepository.findByUserId(orderDto.getUserId());
        System.out.println("optionalUser = " + optionalUser.get());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // 예상 총 금액: 상품 금액 * 결제 수량 + 배송비
            int productPrice = Integer.parseInt(project.getProPrice());
            int orderCnt = orderDto.getOrderCnt();
            int deliveryPay = orderDto.getDeliveryPay();
            int expectedTotalAmount = productPrice * orderCnt + deliveryPay;
            if (orderDto.getTotalAmount() != expectedTotalAmount) {
                throw new RuntimeException("결제 금액이 일치하지 않습니다.");
            }
            Order order = Order.toEntity(orderDto);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("USER not found");
        }
    }

    // 환불 처리
    public void cancelOrder(RefundDto refundDto) throws Exception {
        String imp_uid = refundDto.getImp_uid();
        String reason = refundDto.getReason();
        getToken();

        String url = "https://api.iamport.kr/payments/cancel";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + portOneTokenResponse.getResponse().getAccess_token());
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("imp_uid", imp_uid);
        params.add("reason", reason);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("imp_uid : " + imp_uid + " cancelled successfully...");
            Optional<Order> optionalOrder = orderRepository.findByImpUid(imp_uid);

            if (optionalOrder.isPresent()) {
                Order order = optionalOrder.get();
                order.setOrderState("환불 완료");
                order.setRefundDetail(reason);
                orderRepository.save(order);
                log.info("Order with imp_uid : " + imp_uid + " update to '결제 취소' with '환불 완료'");
            } else {
                log.error("imp_uid : " + imp_uid + " not found in DB");
                throw new RuntimeException("Order not found for cancel....");
            }
        } else {
            log.error("Failed to cancel imp_uid : " + imp_uid + " StatusCode : " + response.getStatusCode());
            throw new RuntimeException("Order cancel failed....");
        }
    }

    //결제 내역 조회 메서드
    public List<OrderDto> getPaymentHistory() {
        log.info("payment history from Order table...");
        List<Order> orderList = orderRepository.findAll();
        List<OrderDto> orderDtoList = orderList.stream()
                .map(OrderDto::EntityToOrderDto)
                .collect(Collectors.toList());
        return orderDtoList;
    }

    // 특정 결제 내역 조회
    public OrderDto findById(String impUid) {
        Optional<Order> optionalOrder = orderRepository.findById(impUid);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            OrderDto orderDto = OrderDto.EntityToOrderDto(order);
            return orderDto;
        } else {
            throw new RuntimeException("해당 결제 정보가 없습니다.");
        }
    }

    public List<OrderDto> findByUserId(User userId) {
        List<Order> orderList = orderRepository.findByUserId(userId);
        return orderList.stream()
                .map(OrderDto::EntityToOrderDto)
                .collect(Collectors.toList());
    }


    @Data
    private static class TokenResponse {
        public String access_token;
        public int now;
        public int expired_at;
    }

    @Component
    @Data
    private static class PortOneTokenResponse {
        public int code;
        public Object message;
        public TokenResponse response;
    }

    @Data
    private static class AUthInfoResponse {
        public int birth;
        public String birthday;
        public boolean certified;
        public int certified_at;
        public boolean foreigner;
        public Object foreigner_v2;
        public Object gender;
        public String imp_uid;
        public String merchant_uid;
        public String name;
        public String origin;
        public String pg_provider;
        public String pg_tid;
        public String phone;
        public Object unique_in_site;
        public String unique_key;
    }

    @Component
    @Data
    private static class PortOneAuthInfoResponse {
        public int code;
        public Object message;
        public AUthInfoResponse response;
    }

}
