package com.guavapay.security;

import com.guavapay.model.type.AccountState;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Principal implements UserDetails {

    private Long id;
    private String login;
    private String password;
    private String fullName;
    private Integer status;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getFullName() {
        return fullName;
    }

    public Integer getStatus() {
        return status;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status != AccountState.LOCKED.ordinal();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
