package com.disi.identyService.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.disi.identyService.model.UserInfo;
import com.disi.identyService.repository.UserInfoRepository;

public class UserDetailsServiceInfo implements UserDetailsService{
 @Autowired
    private UserInfoRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> credential = repository.findByEmail(username);
        return credential.map(UserDetailsInfo::new).orElseThrow(() -> new UsernameNotFoundException("user not found with email :" + username));
    }
}
