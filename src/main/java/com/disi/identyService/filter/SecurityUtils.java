package com.disi.identyService.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.function.Function;


import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.List;
import java.util.Map;

@Component
public class SecurityUtils {
    public static final String SECRET = "MXAFhXFc0uqfqSg3kzestvb8sfaKSiEcEkCpruTOgloYmFhYTI0YjI2NWRjMWVmZTA1MzJlNTZhODI4OWY5Zjc1OTIyYTA2MmU5YzEzNjI1ZTNmM2I1MzY1ODQwMjYyNTYzNmIxODkxOWMzODA0OTFlZDlmODVmMzEwOGM3ZWZhZTc1OGExZjI3YzU0Nzk1NzAyOGQ0OTlkMjgzOTVmYjM=";

    private SecurityUtils() {
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    @SuppressWarnings("deprecation")
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String extractLastname(String token) {
        return (String) getAllClaimsFromToken(token).get("lastname"); // Extraire l'email à partir des claims
    }

    public String extractEmail(String token) {
        return (String) getAllClaimsFromToken(token).get("email"); // Extraire l'email à partir des claims
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> extractRoles(String token) {
        return (List<Map<String, Object>>) getAllClaimsFromToken(token).get("roles"); // Extraire l'email à partir des
                                                                                      // claims
    }

    public Integer extractId(String token) {
        return (Integer) getAllClaimsFromToken(token).get("id"); // Extraire l'email à partir des claims
    }
    // La clé de signature obtenue via getSignKey() est utilisée pour vérifier la
    // signature du JWT.

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
