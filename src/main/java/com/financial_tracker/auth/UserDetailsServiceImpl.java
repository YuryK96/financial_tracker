package com.financial_tracker.auth;

import com.financial_tracker.core.credentials.CredentialsEntity;
import com.financial_tracker.core.credentials.CredentialsRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final CredentialsRepository credentialsRepository;


    public UserDetailsServiceImpl(CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String login) {
        CredentialsEntity credentialsEntity = credentialsRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(String.format("Credential with login %s not found", login)));

        return new CustomUserDetails(

                credentialsEntity.getLogin(),
                credentialsEntity.getPassword(),
                credentialsEntity.getId(),
                credentialsEntity.getAccount().getId(),
                Collections.emptyList()
        );
    }
}
