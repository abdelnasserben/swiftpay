package com.swiftpay.service;

import com.swiftpay.dto.AgencyDto;
import com.swiftpay.mapper.AgencyMapper;
import com.swiftpay.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public class MyUserPrincipal implements UserDetails {

    private final UserService userService;
    private final User user;

    public MyUserPrincipal(UserService userService, User user) {
        this.userService = userService;
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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

    public AgencyDto getAgency() {
        return AgencyMapper.toDto(userService.getAuthenticated().getAgency());
    }
}
