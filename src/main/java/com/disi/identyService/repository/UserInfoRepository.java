package com.disi.identyService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.disi.identyService.model.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer>{
    Optional<UserInfo> findByEmail(String email);
    Optional<UserInfo> findByUserId(int userId);
    UserInfo findById(int id);
    Optional<UserInfo> deleteByUserId(int userId);
    

}
