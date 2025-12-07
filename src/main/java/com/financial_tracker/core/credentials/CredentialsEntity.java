package com.financial_tracker.core.credentials;


import com.financial_tracker.core.BaseEntity;
import com.financial_tracker.core.account.AccountEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Table(name = "credentials")
@Entity
public class CredentialsEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "login", nullable = false, unique = true)
    private String login;


    @Column(name = "password", nullable = false)
    private String password;


    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, updatable = false)
    private AccountEntity account;


    public CredentialsEntity() {
    }

    public CredentialsEntity(UUID id, String login, String password, AccountEntity account) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.account = account;
    }


    public UUID getId() {
        return id;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

}
