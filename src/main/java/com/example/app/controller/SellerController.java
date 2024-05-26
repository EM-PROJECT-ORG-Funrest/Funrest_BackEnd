package com.example.app.controller;


import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.dto.UserDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
import com.example.app.domain.repository.ProjectRepository;
import com.example.app.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("/th/myPage/seller")
public class SellerController {

    private UserRepository userRepository;


    @GetMapping("/seller")
    public void selectUserID1(User user, Model model){
        UserDto userDto = new UserDto();
        userDto.EntityToUserDto(user);
        String userId = userDto.getUserId();
        model.addAttribute("userId", userId);
        log.info("GET /th/myPage/seller/seller .....");

    }

    @PostMapping("/seller")
    public void selectUserID(User user, Model model){
        log.info("post /th/myPage/seller/seller .....");

    }

    @PostMapping("/projectCreate")
    public void InsertProject(ProjectDto projectDto){
      log.info("POST /th/myPage/seller/projectCreate .....");
      //insert문 만들어서 여기서 삽입
        //
    }

}
