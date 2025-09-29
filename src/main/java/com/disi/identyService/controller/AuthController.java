package com.disi.identyService.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.disi.identyService.dto.AuthRequest;
import com.disi.identyService.dto.RefreshTokenRequest;
import com.disi.identyService.model.Role;
import com.disi.identyService.model.UserInfo;
import com.disi.identyService.service.AuthService;
import com.disi.identyService.service.RoleService;

@RestController
@RequestMapping("/users")
public class AuthController {

    @Autowired
    private AuthService service;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @CrossOrigin(origins = "*")
    public String addNewUser(@RequestBody UserInfo user) {
        System.out.println("------------register-------------------------");
        return service.saveUser(user);
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String, String>> getToken(@RequestBody AuthRequest authRequest) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            String accessToken = service.generateToken(authRequest.getUsername(), 1000 * 60 * 20l);
            String refreshToken = service.generateRefreshToken(authRequest.getUsername(), 1000 * 60 * 30l);
            Map<String, String> response = new HashMap<>();
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);
            return ResponseEntity.ok(response);
        } else {
            throw new RuntimeException("invalid access");
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody RefreshTokenRequest refresh) {
        if (service.validateRefreshToken(refresh)) {
            String login = service.getUsernameFromToken(refresh);
            AuthRequest authRequest = new AuthRequest(login, null);
            Map<String, String> response = new HashMap<>();
            String accessToken = service.generateToken(authRequest.getUsername(), 1000 * 60 * 6l);
            response.put("accessToken", accessToken);
            return ResponseEntity.ok(response);
        } else {
            throw new RuntimeException("invalid access");
        }
    }
    // @GetMapping("/all")
    // @CrossOrigin(origins = "*")
    // public List<UserInfo> getAllUserInfo() {
    // List<UserInfo> users = service.getAllUserInfo();

    // System.out.println(users.size());
    // return users;
    // }

    // @GetMapping("/all/{page}/{size}")
    // @CrossOrigin(origins = "*")
    // public List<UserInfo> getAlUserInfo(@PathVariable int page, @PathVariable int size) {
    //     Page<UserInfo> users = service.getAlUserInfo(page, size);
    //     System.out.println(users.getSize());
    //     return users.getContent();
    // }
    @GetMapping("/all")
    @CrossOrigin(origins = "*")
    public List<UserInfo> getAlUserInfo() {
        List<UserInfo> users = service.getAlUserInfo();
        return users;
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    public UserInfo getUser(@PathVariable int id) {
        return service.getUserInfo(id);
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "*")
    public UserInfo putUser(@PathVariable("id") int userId, @RequestBody UserInfo userInfo) {
        return service.updateUser(userId, userInfo);
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*")
    public void deleteUser(@PathVariable("id") int userId) {
        service.deleteUser(userId);
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        service.validateToken(token);
        return "Token is valid";
    }

    @GetMapping("/{userId}/roles")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Set<Role>> loadUserRoles(@PathVariable int userId) {
        try {
            Set<Role> roles = service.getRoleUserInfo(userId);
            return ResponseEntity.ok(roles);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{userId}/roles")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String, String>> updateUserRoles(@PathVariable int userId,
            @RequestBody List<Role> roles) {
        System.out.println("****************************" + userId + "****************************" + roles);
        Map<String, String> response = new HashMap<>();

        try {
            service.updateUserRoles(userId, roles);
            response.put("message", "Rôles mis à jour avec succès");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("error", "Erreur : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/{userId}/role")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String, String>> revoqueUserRole(@PathVariable int userId,
            @RequestBody List<Role> roles) {
        Map<String, String> response = new HashMap<>();

        try {
            service.revoquerUserRole(userId, roles);
            response.put("message", "Rôle révoqué avec succès");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("error", "Erreur : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // *****role */
    @PostMapping("/role")
    @CrossOrigin(origins = "*")
    public String addNewRole(@RequestBody Role role) {
        return roleService.saveRole(role);
    }

    @GetMapping("/role/{id}")
    @CrossOrigin(origins = "*")
    public Role getRole(@PathVariable("id") int roleId) {
        return roleService.getRole(roleId);

    }

    @GetMapping("/role")
    @CrossOrigin(origins = "*")
    public List<Role> getAllRole() {
        System.out.println("*********************************test");
        List<Role> roles = roleService.getAllRoles();
        System.out.println(roles.size());
        return roles;
    }

    @PutMapping("/role/{id}")
    @CrossOrigin(origins = "*")
    public Role updateRole(@PathVariable("id") int id, @RequestBody Role role) {
        // Role role = roleService.getRole(id);
        System.out.println();
        return roleService.updateRole(id, role);
    }

    @DeleteMapping("/role/{id}")
    @CrossOrigin(origins = "*")
    public void deleteRole(@PathVariable("id") int id) {
        roleService.deleteRole(id);
    }
}
