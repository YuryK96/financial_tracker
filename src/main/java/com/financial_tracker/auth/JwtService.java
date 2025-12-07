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

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expire}")
    private Duration jwtExpire;


    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userDetails.getUsername());

        log.debug("JWT Expire string {}", jwtExpire.toString());
        Date issueDate = new Date();
        Date expirationDate = new Date(issueDate.getTime() + jwtExpire.toMillis());

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(claims)
                .issuedAt(issueDate)
                .expiration(expirationDate)
                .signWith(getSignKey())
                .compact();

    }

    public String getUserNameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }


    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
