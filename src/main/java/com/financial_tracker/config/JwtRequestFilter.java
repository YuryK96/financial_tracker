package com.financial_tracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.financial_tracker.auth.CustomUserDetails;
import com.financial_tracker.auth.JwtService;
import com.financial_tracker.shared.dto.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);
    private final ObjectMapper objectMapper;

    public JwtRequestFilter(JwtService jwtService) {
        this.jwtService = jwtService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");


        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            try {
                String username = jwtService.getUserNameFromAccessToken(jwt);
                UUID userId = jwtService.extractUserIdFromAccessToken(jwt);


                if (username != null && userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    List<GrantedAuthority> authorities = new ArrayList<>();

                    CustomUserDetails customUserDetails = new CustomUserDetails(
                            username,
                            "",
                            userId,
                            authorities
                    );
                    UsernamePasswordAuthenticationToken token =
                            new UsernamePasswordAuthenticationToken(customUserDetails, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(token);
                    log.debug("Authentication set for user: {}", username);
                }

            } catch (ExpiredJwtException e) {
                log.debug("Token expired, Spring Security will handle it");
                sendError(response, "Token expired");

            } catch (Exception e) {
                log.debug("Token invalid: {}, Spring Security will handle it", e.getMessage());
                sendError(response, "Invalid token");
            }
        }


        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        log.warn("Returning error: {}", message);


        ErrorResponse error = new ErrorResponse(
                "Unauthorized",
                new String[]{message},
                LocalDateTime.now()
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), error);
    }
}