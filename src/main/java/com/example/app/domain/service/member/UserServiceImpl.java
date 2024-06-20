package com.example.app.domain.service.member;

import com.example.app.domain.entity.User;
import com.example.app.domain.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class UserServiceImpl {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public ResponseEntity<Map<String, String>> findIdCheck(String userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        Map<String, String> response = new HashMap<>();

        if (user.isEmpty()) {
            response.put("message", "가입된 정보가 없습니다!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            response.put("message", "이미 가입된 이메일입니다!");
            return ResponseEntity.ok(response);
        }
    }

    public ResponseEntity<Map<String, String>> findIdCheck2(String userId, String randomValue) throws Exception {
        Optional<User> user = userRepository.findByUserId(userId);
        Map<String, String> response = new HashMap<>();

        if (user.isEmpty()) {
            response.put("message", "가입된 정보가 없습니다!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            response.put("message", "이메일을 확인해 주세요!");
            sendEmail(userId, randomValue);
            return ResponseEntity.ok(response);
        }
    }


    public String randomValue(){
        Random random = new Random();

        List<String> list = new ArrayList<>();
        for(int i=0; i<30; i++) {
            list.add(String.valueOf(random.nextInt(10)));
        }
        for(int i=0; i<30; i++) {
            list.add(String.valueOf((char)(random.nextInt(26)+65)));
        }

        Collections.shuffle(list);

        StringBuilder sb = new StringBuilder();
        for (String item : list){
            sb.append(item);
        }
        String randomValue = sb.toString();
        return randomValue;
    }

    public void sendEmail(String userId, String randomValue) throws Exception{
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userId);
        message.setSubject("[펀레스트] 비밀번호 찾기 인증 이메일입니다.");
        message.setText("다음 링크를 클릭해 비밀번호를 변경 완료해 주세요.\n");
        message.setText("http://localhost:8080/th/member/findPwUrl/"+randomValue);

        javaMailSender.send(message);
    }

    public void findById(String userId, String userPwEncode){
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUserPw(userPwEncode);

        userRepository.save(user);

    }


}
