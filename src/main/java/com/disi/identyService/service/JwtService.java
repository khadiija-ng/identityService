
package com.disi.identyService.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.disi.identyService.dto.RefreshTokenRequest;
import com.disi.identyService.model.UserInfo;
import com.disi.identyService.repository.UserInfoRepository;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    public static final String SECRET = "MXAFhXFc0uqfqSg3kzestvb8sfaKSiEcEkCpruTOgloYmFhYTI0YjI2NWRjMWVmZTA1MzJlNTZhODI4OWY5Zjc1OTIyYTA2MmU5YzEzNjI1ZTNmM2I1MzY1ODQwMjYyNTYzNmIxODkxOWMzODA0OTFlZDlmODVmMzEwOGM3ZWZhZTc1OGExZjI3YzU0Nzk1NzAyOGQ0OTlkMjgzOTVmYjM=";

    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }
    public String generateRefreshToken(String userName, Long dureeValiditer) {
        UserInfo user = userInfoRepository.findByEmail(userName).get();
        Map<String, Object> claims = new HashMap<>();
        return createRefreshToken(claims, user, dureeValiditer);
    }
    public String generateToken(String userName, Long dureeValiditer) {
    UserInfo user = userInfoRepository.findByEmail(userName).orElseThrow(
        () -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + userName)
    );
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, user, dureeValiditer);
}

    private String createRefreshToken(Map<String, Object> claims, UserInfo user, Long dureeValiditer) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + dureeValiditer))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

    }

    public boolean validateRefreshToken(RefreshTokenRequest token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token.getToken());
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(getSignKey())
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    public String getUsernameFromToken(RefreshTokenRequest token) {
        return getClaimsFromToken(token.getToken()).getSubject(); 
    }

    private String createToken(Map<String, Object> claims, UserInfo user, Long dureeValiditer) {
        List<Map<String, Object>> rolesList = user.getRoles().stream()
                .map(role -> {
                    Map<String, Object> roleMap = new HashMap<>();
                    roleMap.put("roleId", role.getRoleId());
                    roleMap.put("roleName", role.getRoleName());
                    return roleMap;
                })
                .collect(Collectors.toList());
        System.out.println(rolesList);
        return Jwts.builder()
                .setClaims(claims)
                .claim("id", user.getUserId())
                .claim("firstname", user.getFirstname())
                .claim("lastname", user.getLastname())
                .claim("email", user.getEmail())
                .claim("roles", rolesList)
                .claim("isEnabled", user.isEnabled())
                .setSubject(user.getFirstname())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + dureeValiditer))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
