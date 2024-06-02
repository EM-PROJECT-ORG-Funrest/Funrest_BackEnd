package com.example.app.config.auth;

import com.example.app.domain.dto.UserDto;
import com.example.app.domain.entity.User;
import com.example.app.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        //System.out.println("PrincipalDetailsService's loadUserByUsername : " + userId);
        System.out.println("PrincipalDetailsService's loadUserByUsername : " + userId);

        Optional<User> userOptional = userRepository.findByUserId(userId);

        //System.out.println("userOptional role : " + userOptional.get().getRole()); //ROLE_USER

        if(userOptional.isEmpty()){
            throw new UsernameNotFoundException(userId);
        }

        //Entity -> Dto
        UserDto userDto = new UserDto();
        User user = userOptional.get();
        userDto.setUserId(user.getUserId());
        userDto.setUserPw(user.getUserPw());
        userDto.setRole(user.getRole());

        System.out.println(userDto.toString());

        return new PrincipalDetails(userDto);
    }
}
