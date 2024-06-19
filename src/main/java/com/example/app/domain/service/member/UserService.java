package com.example.app.domain.service.member;

import com.example.app.domain.dto.UserDto;
import com.example.app.domain.entity.User;
import com.example.app.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 모든 유저 정보 조회
    public Page<UserDto> getAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        Page<UserDto> userDtoPage = userPage.map(UserDto::EntityToUserDto);
        return userDtoPage;
    }

    // 관리자 회원관리 페이지에서 체크된 유저 삭제
    public void deleteUsersByIds(List<String> ids) {
        System.out.println("ids = " + ids);
        for (String id : ids) {
            userRepository.deleteById(id);
        }
    }




}
