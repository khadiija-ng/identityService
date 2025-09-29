package com.disi.identyService.service;

import java.util.List;

import java.util.Set;

import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.disi.identyService.advice.UserNotFoundException;
import com.disi.identyService.dto.RefreshTokenRequest;
import com.disi.identyService.model.Role;
import com.disi.identyService.model.UserInfo;
import com.disi.identyService.repository.UserInfoRepository;



@Service
public class AuthService {

    @Autowired
    private UserInfoRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String saveUser(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "Utilisateur ajouté au systéme";
    }

    public UserInfo getUserInfo(int userId) {
        UserInfo user = repository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return user;
    }

    public Set<Role> getRoleUserInfo(int userId) {
        UserInfo user = repository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return user.getRoles();
    }

    // public Page<UserInfo> getAlUserInfo(int pageNo, int pageSize) {
    //     Pageable pageable = PageRequest.of(pageNo, pageSize);
    //     System.out.println(pageNo + "************************ " + pageSize);
    //     return repository.findAll(pageable);
    // }
     public List<UserInfo> getAlUserInfo() {
        return repository.findAll();
    }

    public void updateUserRoles(int userId, List<Role> roles) {
        UserInfo user = repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Set<Role> roleSet = new HashSet<>(roles);
        user.setRoles(roleSet);
        repository.save(user);
    }

    public void revoquerUserRole(int userId, List<Role> roles) {
        UserInfo user = repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        System.out.println("************************************" + roles);
        Set<Role> updatedRoles = user.getRoles().stream()
                .filter(role -> !roles.stream().anyMatch(r -> r.getRoleId() == role.getRoleId()))
                .collect(Collectors.toSet());

        user.setRoles(updatedRoles);
        repository.save(user);
    }

    public String generateToken(String username, Long duree) {
        return jwtService.generateToken(username, duree);
    }

    public String generateRefreshToken(String username, Long duree) {
        return jwtService.generateRefreshToken(username, duree);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
     public boolean validateRefreshToken(RefreshTokenRequest token) {
       return jwtService.validateRefreshToken(token);
    }
    public String getUsernameFromToken(RefreshTokenRequest token) {
        return jwtService.getUsernameFromToken(token); 
    }

    public UserInfo updateUser(int id, UserInfo user) {
        UserInfo users = repository.findByUserId(id).orElseThrow(() -> new UserNotFoundException(id));
        users.setFirstname(user.getFirstname());
        users.setLastname(user.getLastname());
        users.setEmail(user.getEmail());
        users.setRoles(user.getRoles());
        users.setEnabled(user.isEnabled());
        return repository.save(users);

    }

    public void deleteUser(int userId) {
        UserInfo user = repository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException(userId));
        repository.delete(user);

    }

}
