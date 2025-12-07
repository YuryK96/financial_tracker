package com.financial_tracker.auth;

import com.financial_tracker.core.credentials.CredentialsEntity;
import com.financial_tracker.core.credentials.CredentialsRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final CredentialsRepository credentialsRepository;

    public CustomUserDetailsService(CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }


    public UserDetails loadUserByUsername(String login) {
        CredentialsEntity credentialsEntity = credentialsRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(String.format("Credential with login %s not found", login)));

        return new User(
                credentialsEntity.getLogin(),
                credentialsEntity.getPassword(),
                Collections.emptyList()
        );
    }
}
