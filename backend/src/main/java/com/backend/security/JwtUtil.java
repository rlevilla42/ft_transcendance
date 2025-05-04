package com.backend.security;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "JWT-SECRET-KEY"; // 🔒 À placer dans le `.env` ou `application.properties` plus tard
    
    // Durée de validité du token (ex : 24h * 7)
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;

    // ✅ Génère un token pour un username
    public String generateToken(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
        .compact();
    };

    public Claims getClaims (String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY)
        .parseClaimsJws(token).getBody();
    };

    public String extractUsername(String username) {
        return getClaims(username).getSubject();
    };

    public boolean isTokenValid(String token) {
        try {
            return getClaims(token).getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    };
}