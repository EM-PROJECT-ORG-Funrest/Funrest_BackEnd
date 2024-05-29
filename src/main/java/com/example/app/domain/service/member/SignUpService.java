package com.example.app.domain.service.member;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SignUpService {

    @Autowired
    private JavaMailSender javaMailSender;
    private int authNumber;

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
        message.setText("펀레스트 가입 신청을 해주셔서 감사드립니다.\n" + "다음 인증번호를 정확히 입력하고 인증을 완료해 주세요.\n" + authNumber);

        javaMailSender.send(message);
    }

    public void userJoin() {

    }
}
