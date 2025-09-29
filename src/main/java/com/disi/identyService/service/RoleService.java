package com.disi.identyService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.disi.identyService.model.Role;
import com.disi.identyService.repository.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository repository;

    public String saveRole(Role role) {
        repository.save(role);
        return "Role ajouté avec succés";
    }

    public List<Role> getAllRoles() {
        return repository.findAll();
    }

    public Role getRole(int roleId) {
        return repository.findById(roleId);
    }

    public Role updateRole(int id, Role roles) {
        Role role = repository.findById(id);
        System.out.println("*************************************"+role.getRoleName());
        role.setRoleName(roles.getRoleName());
        System.out.println("*************************************"+role);
        return repository.save(role);

    }

    public void deleteRole(int id) {
        Role role = repository.findById(id);
        repository.delete(role);

    }
}