package com.example.app.controller.admin;

import com.example.app.domain.entity.User;
import com.example.app.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.findByRole("ROLE_ADMIN").isEmpty()) {
            User admin = new User();
            admin.setUserId("admin");
            admin.setUserPw(passwordEncoder.encode("1234"));
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);
        }
    }
}
