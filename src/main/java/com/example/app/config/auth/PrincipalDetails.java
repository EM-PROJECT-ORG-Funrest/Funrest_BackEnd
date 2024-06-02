package com.example.app.config.auth;

import com.example.app.domain.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrincipalDetails implements UserDetails{

    private UserDto userDto;

//    public PrincipalDetails(UserDto userDto) {
//        this.userDto = userDto;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new SimpleGrantedAuthority(userDto.getRole()));

        return collection;
    }

    @Override
    public String getPassword() {
        return userDto.getUserPw();
    }

    @Override
    public String getUsername() {
        return userDto.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
