package com.financial_tracker.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    @Value("${app.jwt.access_secret}")
    private String jwtAccessSecret;

    @Value("${app.jwt.access_expire}")
    private Duration jwtAccessExpire;

    @Value("${app.jwt.refresh_secret}")
    private String jwtRefreshSecret;

    @Value("${app.jwt.refresh_expire}")
    private Duration jwtRefreshExpire;


    public String generateAccessToken(String username, UUID userId, UUID accountId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        claims.put("userId", userId);
        claims.put("accountId", accountId);


        Date issueDate = new Date();
        Date expirationDate = new Date(issueDate.getTime() + jwtAccessExpire.toMillis());

        return Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(issueDate)
                .expiration(expirationDate)
                .signWith(getAccessSignKey())
                .compact();

    }

    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);

        Date issueDate = new Date();
        Date expirationDate = new Date(issueDate.getTime() + jwtRefreshExpire.toMillis());

        return Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(issueDate)
                .expiration(expirationDate)
                .signWith(getRefreshSignKey())
                .compact();

    }

    public String getUserNameFromRefreshToken(String token) {
        return getAllClaimsFromRefreshToken(token).getSubject();
    }

    public String getUserNameFromAccessToken(String token) {
        return getAllClaimsFromAccessToken(token).getSubject();
    }


    public UUID extractUserIdFromAccessToken(String token) {
        String userIdStr = getAllClaimsFromAccessToken(token).get("userId", String.class);
        return UUID.fromString(userIdStr);
    }

    public UUID extractAccountIdFromAccessToken(String token) {
        String accountIdStr = getAllClaimsFromAccessToken(token).get("accountId", String.class);
        return UUID.fromString(accountIdStr);
    }

    private SecretKey getAccessSignKey() {
        return Keys.hmacShaKeyFor(jwtAccessSecret.getBytes());
    }

    private SecretKey getRefreshSignKey() {
        return Keys.hmacShaKeyFor(jwtRefreshSecret.getBytes());
    }

    private Claims getAllClaimsFromAccessToken(String token) {
        return Jwts.parser()
                .verifyWith(getAccessSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private Claims getAllClaimsFromRefreshToken(String token) {
        return Jwts.parser()
                .verifyWith(getRefreshSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
