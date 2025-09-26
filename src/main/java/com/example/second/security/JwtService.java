package com.example.second.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;
    private Key key;

    private static final long EXPIRATION_MS = 5 * 60 * 60 * 1000; // 5 hours

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION_MS);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("JWT invalid or expired: " + e.getMessage(), e);
        }
    }

    public String extractSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        Object role = extractAllClaims(token).get("role");
        return role != null ? role.toString() : null;
    }

    public boolean isTokenValid(String token, String expectedSubject) {
        final String subject = extractSubject(token);
        Date expiration = extractAllClaims(token).getExpiration();
        return (subject.equals(expectedSubject) && expiration.after(new Date()));
    }
}
