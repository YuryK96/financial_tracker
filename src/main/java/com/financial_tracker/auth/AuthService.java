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
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(CredentialsRepository credentialsRepository, AuthenticationManager authenticationManager, AccountService accountService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.credentialsRepository = credentialsRepository;
        this.authenticationManager = authenticationManager;

        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public UserDetails loadUserByUsername(String login) {
        CredentialsEntity credentialsEntity = credentialsRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(String.format("Credential with login %s not found", login)));

        return new User(
                credentialsEntity.getLogin(),
                credentialsEntity.getPassword(),
                Collections.emptyList()
        );
    }

    public JWTResponse login(JwtRequest jwtRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                jwtRequest.login(),
                jwtRequest.password()
        ));

        UserDetails userDetails = loadUserByUsername(jwtRequest.login());
        String token = jwtService.generateToken(userDetails);

        return new JWTResponse(token);
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

        UserDetails userDetails = loadUserByUsername(registrationRequest.login());
        String token = jwtService.generateToken(userDetails);

        return new JWTResponse(token);
    }

}
