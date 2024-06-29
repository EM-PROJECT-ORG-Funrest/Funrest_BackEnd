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

    // 가입된 ID인지 찾아주는 기능 : findId 에서 사용
    public ResponseEntity<Map<String, String>> findIdCheck(String userId) {
        // userId로 userEntity 가져와서
        Optional<User> user = userRepository.findByUserId(userId);
        Map<String, String> response = new HashMap<>();


        if (user.isEmpty()) {
            //만약 비어있으면 전송
            response.put("message", "가입된 정보가 없습니다!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            // 안비어있으면 전송
            response.put("message", "이미 가입된 이메일입니다!");
            return ResponseEntity.ok(response);
        }
    }

    // 이메일 전송을 위한 userId 유효성 검사
    public ResponseEntity<Map<String, String>> findIdCheck2(String userId, String randomValue) throws Exception {

        // userId로 유저 정보 가져오기
        Optional<User> user = userRepository.findByUserId(userId);
        Map<String, String> response = new HashMap<>();

        if (user.isEmpty()) {
            // userEntity가 비어있으면 전송
            response.put("message", "가입된 정보가 없습니다!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            // userEntity가 비어있지않으면 메일 전송
            response.put("message", "이메일을 확인해 주세요!");
            sendEmail(userId, randomValue);
            return ResponseEntity.ok(response);
        }
    }

    
    //랜덤값 만들어주기
    public String randomValue(){
        Random random = new Random();
        
        //총 58자리 랜던값 생성
        List<String> list = new ArrayList<>();
        for(int i=0; i<30; i++) {
            list.add(String.valueOf(random.nextInt(10)));
        }
        for(int i=0; i<30; i++) {
            list.add(String.valueOf((char)(random.nextInt(26)+65)));
        }

        //list에 저장된 랜덤값배열 무작위로 섞기
        Collections.shuffle(list);

        // list에 배열형태로 저장된 랜덤값을 String으로 반환 해주기
        StringBuilder sb = new StringBuilder();
        for (String item : list){
            sb.append(item);
        }
        String randomValue = sb.toString();
        return randomValue;
    }

    //메일 전송 서비스
    public void sendEmail(String userId, String randomValue) throws Exception{
        // 메일 전송 
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userId);
        message.setSubject("[펀레스트] 비밀번호 찾기 인증 이메일입니다.");
        message.setText("다음 링크를 클릭해 비밀번호를 변경 완료해 주세요.\n");
        // url에 랜덤값 붙여서 전송
        message.setText("http://funrest.store/th/member/findPwUrl/"+randomValue);

        javaMailSender.send(message);
    }

    // 재설정된 userPw 저장해주기
    public void findById(String userId, String userPwEncode){
        // Optional<User> 값 그냥 userEntity로 변환 해주는 방법
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 받아온 인코딩된 userPw userEntity에 저장 
        user.setUserPw(userPwEncode);
        // 저장
        userRepository.save(user);

    }


}
