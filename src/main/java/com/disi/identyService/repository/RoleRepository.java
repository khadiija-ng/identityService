package com.disi.identyService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.disi.identyService.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findById(int id);
    Role findByRoleId(int id);
}
