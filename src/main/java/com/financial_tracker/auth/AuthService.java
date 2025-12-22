package com.financial_tracker.auth;


import com.financial_tracker.account_management.AccountService;
import com.financial_tracker.auth.dto.request.JwtRequest;
import com.financial_tracker.auth.dto.request.RegistrationRequest;
import com.financial_tracker.auth.dto.response.JWTResponse;
import com.financial_tracker.core.account.AccountEntity;
import com.financial_tracker.core.credentials.CredentialsEntity;
import com.financial_tracker.core.credentials.CredentialsRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {
    private final CredentialsRepository credentialsRepository;
    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(CredentialsRepository credentialsRepository, AuthenticationManager authenticationManager, AccountService accountService, UserDetailsServiceImpl userDetailsServiceImpl, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.credentialsRepository = credentialsRepository;
        this.authenticationManager = authenticationManager;

        this.accountService = accountService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public JWTResponse login(JwtRequest jwtRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                jwtRequest.login(),
                jwtRequest.password()
        ));

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();


        String accessToken = jwtService.generateAccessToken(userDetails.getUsername(), userDetails.getUserId(), userDetails.getAccountId());

        String refreshToken = jwtService.generateRefreshToken(userDetails.getUsername());

        return new JWTResponse(accessToken, refreshToken);
    }


    public JWTResponse refresh(String refreshToken) {

        String username = jwtService.getUserNameFromRefreshToken(refreshToken);

        CustomUserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
        String accessToken = jwtService.generateAccessToken(userDetails.getUsername(), userDetails.getUserId(), userDetails.getAccountId());


        return new JWTResponse(accessToken, null);
    }

    @Transactional
    public JWTResponse registration(RegistrationRequest registrationRequest) {
        if (credentialsRepository.existsByLogin(registrationRequest.login())) {
            throw new IllegalArgumentException("User with this login already exists");

        }

        AccountEntity newAccount = accountService.createAccount(registrationRequest.accountName());


        CredentialsEntity credentials = new CredentialsEntity();
        credentials.setAccount(newAccount);
        credentials.setLogin(registrationRequest.login());

        String password = passwordEncoder.encode(registrationRequest.password());
        credentials.setPassword(password);

        credentialsRepository.save(credentials);

        CustomUserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(registrationRequest.login());

        String accessToken = jwtService.generateAccessToken(userDetails.getUsername(), userDetails.getUserId(), userDetails.getAccountId());

        String refreshToken = jwtService.generateRefreshToken(userDetails.getUsername());

        return new JWTResponse(accessToken, refreshToken);
    }

}
