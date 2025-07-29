package com.application.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.Base64;

@Component
public class JwtUtil {
    private final Key SECRET_KEY;
    private final Key SECRET_KEY_2;

    public JwtUtil(@Value("${jwt.access.secret}") String accessSecret,
                   @Value("${jwt.refresh.secret}") String refreshSecret) {
        SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(accessSecret));
        SECRET_KEY_2 = Keys.hmacShaKeyFor(Base64.getDecoder().decode(refreshSecret));
    }

    // ==== GENERATE TOKENS ====

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        return createToken(claims, userDetails.getUsername(), SECRET_KEY, 1000L * 60 * 60 * 10); // 10 hours
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return createToken(claims, userDetails.getUsername(), SECRET_KEY_2, 1000L * 60 * 60 * 24 * 7); // 7 days
    }

    private String createToken(Map<String, Object> claims, String subject, Key key, long expirationMillis) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    // ==== EXTRACT CLAIMS ====

    private Claims extractAllClaims(String token, Key key) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Key key, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, key);
        return claimsResolver.apply(claims);
    }

    // ==== EXTRACT USERNAMES ====

    public String extractUsername(String token) {
        return extractClaim(token, SECRET_KEY, Claims::getSubject);
    }

    public String extractUsernameFromRefreshToken(String token) {
        return extractClaim(token, SECRET_KEY_2, Claims::getSubject);
    }

    // ==== VALIDATION ====

    public boolean isTokenExpired(String token, Key key) {
        Date expiration = extractClaim(token, key, Claims::getExpiration);
        return expiration.before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, SECRET_KEY));
    }

    public boolean validateRefreshToken(String token) {
        try {
            return !isTokenExpired(token, SECRET_KEY_2);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAccessToken(String token) {
        final Claims claims = extractAllClaims(token, SECRET_KEY);
        return "access".equals(claims.get("type"));
    }
}