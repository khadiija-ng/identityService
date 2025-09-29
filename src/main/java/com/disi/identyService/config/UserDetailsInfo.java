package com.disi.identyService.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.disi.identyService.model.UserInfo;

public class UserDetailsInfo implements UserDetails{

    private String username;
    private String password;

    public UserDetailsInfo(UserInfo userInfo) {
        this.username = userInfo.getEmail();
        this.password = userInfo.getPassword();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

}
