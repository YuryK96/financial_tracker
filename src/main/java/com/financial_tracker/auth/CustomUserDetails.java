package com.financial_tracker.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

public class CustomUserDetails implements UserDetails {


    private final String username;
    private final String password;
    private final UUID accountId;
    private final UUID userId;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String username, String password, UUID userId, UUID accountId, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.userId = userId;
        this.accountId = accountId;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public UUID getUserId() {
        return userId;
    }
    public UUID getAccountId() {
        return accountId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
