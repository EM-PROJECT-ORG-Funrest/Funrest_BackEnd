package com.example.app.controller.MyPageController.BuyerController;

import com.example.app.config.auth.logoutHandler.CustomLogoutSuccessHandler;
import com.example.app.domain.dto.NotifyDto;
import com.example.app.domain.dto.ProjectDto;
import com.example.app.config.auth.jwt.JwtTokenProvider;
import com.example.app.config.auth.logoutHandler.CustomLogoutHandler;
import com.example.app.domain.dto.UserDto;
import com.example.app.domain.entity.User;
import com.example.app.domain.repository.UserRepository;
import com.example.app.domain.service.myPage.BuyerServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/th/myPage/buyer")
public class BuyerController {

    private final String UPLOAD_PATH = "http://localhost:8080/upload/";

    @Value("${portOne.rest-api}")
    private String portOne_API;
    @Value("${portOne.secret}")
    private String portOne_SECRET;

    @Autowired
    private BuyerServiceImpl buyerService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;


    @GetMapping("/buyer")
    public String buyer(Model model) {
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
        String snsType = user.getSnsType(); // snsType 가져오기

        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        model.addAttribute("OrderCnt",OrderCnt);
        model.addAttribute("NotifyCnt",NotifyCnt);
        model.addAttribute("isSnsUser", snsType != null);
        return "th/myPage/buyer/buyer";
    }

    private PortOneTokenResponse portOneTokenResponse;
    private PortOneAuthInfoResponse portOneAuthInfoResponse;

    @GetMapping("/getToken")
    public @ResponseBody  void getToken(){
        log.info("GET /getToken....");

        //URL
        String url = "https://api.iamport.kr/users/getToken";

        //HEADER
        HttpHeaders headers = new HttpHeaders();

        //PARAMS
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("imp_key",portOne_API);
        params.add("imp_secret",portOne_SECRET);

        //ENTITY
        HttpEntity< MultiValueMap<String, String>> entity = new HttpEntity<>(params,headers);

        //REQUEST
        RestTemplate rt = new RestTemplate();
        ResponseEntity<PortOneTokenResponse> response = rt.exchange(url, HttpMethod.POST, entity, PortOneTokenResponse.class);

        //RESPONSE
        System.out.println(response.getBody());
        this.portOneTokenResponse = response.getBody();
    }

    @GetMapping(value ="getAuthInfo/{imp_uid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody PortOneAuthInfoResponse getAuthInfo(@PathVariable("imp_uid") String imp_uid){
        getToken();
        log.info("GET /getAuthInfo..." + imp_uid);

        //URL
        String url = "https://api.iamport.kr/certifications/" + imp_uid;

        //HEADER
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + portOneTokenResponse.getResponse().getAccess_token());
        //PARAMS
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        //ENTITY
        HttpEntity< MultiValueMap<String, String>> entity = new HttpEntity<>(params,headers);

        //REQUEST
        RestTemplate rt = new RestTemplate();
        ResponseEntity<PortOneAuthInfoResponse> response = rt.exchange(url, HttpMethod.GET, entity, PortOneAuthInfoResponse.class);

        //RESPONSE
        System.out.println(response.getBody());

        return response.getBody();
    }

    @GetMapping("/editProfile")
    public void editProfile(Model model){

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = buyerService.findUserByUserId(userId);

        UserDto userDto =  UserDto.EntityToUserDto(user);
        model.addAttribute("userDto", userDto);


    }

    @PostMapping("/editProfileSave")
    public String editProfileSave(UserDto userDto){
        User user = User.UserDtoToEntity(userDto);
        userRepository.save(user);

        return "redirect:/th/myPage/buyer/buyer";
    }

    @GetMapping("/editPassword")
    public void editPassword(){
        log.info("editPassword INVOKE....");
    }

    @PostMapping("/editPasswordSave")
    public  ResponseEntity<Map<String, String>> editPassword(@RequestParam("password1") String password1,
                                                  @RequestParam("password2") String password2,
                                                  @RequestParam("password3") String password3){
        log.info("editPasswordSave INVOKE....");

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = buyerService.findUserByUserId(userId);
        String userPw = user.getUserPw();
        System.out.println(userPw);

        Map<String, String> response = new HashMap<>();

        boolean passwordMatched = passwordEncoder.matches(password1, userPw);
        if (!passwordMatched) {
            response.put("status", "error");
            response.put("message", "기존 비밀번호가 일치하지 않습니다.");
            return ResponseEntity.badRequest().body(response);
        }


        // 비밀번호 변경 로직
        String encodedNewPassword = passwordEncoder.encode(password2);
        user.setUserPw(encodedNewPassword);
        userRepository.save(user);

        // 비밀번호 변경 성공 시 응답
        response.put("status", "success");
        response.put("message", "비밀번호가 성공적으로 변경되었습니다.");
        response.put("redirectUrl", "/th/myPage/buyer/buyer"); // 리다이렉트할 URL 추가
        return ResponseEntity.ok(response);
    }

    @GetMapping("/signOut")
    public void signOut(){
        log.info("signOut INVOKE....");
    }

    @GetMapping("/deleteSign")
    public void deleteSign(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("deleteSign invoked....");
        // 회원 정보 삭제
        buyerService.deleteByUserId(userId);
        //user refreshKey 삭제
        customLogoutSuccessHandler.onLogoutSuccess(request, response, SecurityContextHolder.getContext().getAuthentication());
    }


    @Data
    private static class TokenResponse{
        public String access_token;
        public int now;
        public int expired_at;
    }

    @Component
    @Data
    private static class PortOneTokenResponse{
        public int code;
        public Object message;
        public TokenResponse response;
    }

    @Data
    private static class AUthInfoResponse{
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
    private static class PortOneAuthInfoResponse{
        public int code;
        public Object message;
        public AUthInfoResponse response;
    }


    @GetMapping("/notify")
    public String showNotify(Model model) {
        log.info("get /th/myPage/buyer/notify showNotify()");

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            List<NotifyDto> notifyDtos = buyerService.getAllNotifyByUserId(userId);
            List<ProjectDto> projectDtos = buyerService.getAllProjectByProCode(notifyDtos);

            for(ProjectDto projectDto : projectDtos) {
                projectDto.setMainPageImgPath(UPLOAD_PATH+projectDto.getStoredFileName().getFirst());
            }

            log.info("projectDtos result : " + projectDtos);

            model.addAttribute("projectDtos", projectDtos);
            return "th/myPage/buyer/buyer :: projectCardsFragment";
        } catch(RuntimeException e) {
            //ResponseEntity("Can not found User or Project", HttpStatus.BAD_REQUEST);
            return null;
        }
    }
}
