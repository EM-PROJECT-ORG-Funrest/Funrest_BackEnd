package com.example.app.domain.service.member;

import com.example.app.domain.dto.UserDto;
import com.example.app.domain.entity.User;
import com.example.app.domain.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
public class SignUpService {

    private int authNumber;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void makeAuthNumber(HttpSession session) {
        Random r = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }

        authNumber = Integer.parseInt(randomNumber);

        session.setAttribute("code", authNumber);
    }

    public void sendEmail(String id, HttpSession session) throws Exception{
        makeAuthNumber(session);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(id);
        message.setSubject("[펀레스트] 회원가입 인증 이메일입니다.");
        message.setText("다음 인증번호를 정확히 입력하고 인증을 완료해 주세요.\n" + authNumber);

        javaMailSender.send(message);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean userJoin(UserDto userDto) throws Exception {
        System.out.println("service userDto : " + userDto);
        Optional<User> userOptional = userRepository.findById(userDto.getUserId());
        if(!userOptional.isEmpty()) {
            return false;
        }
        userDto.setUserPw(passwordEncoder.encode(userDto.getUserPw()));
        userDto.setRole("ROLE_USER");
        User user = User.UserDtoToEntity(userDto);
        userRepository.save(user);
        return true;
    }
}
