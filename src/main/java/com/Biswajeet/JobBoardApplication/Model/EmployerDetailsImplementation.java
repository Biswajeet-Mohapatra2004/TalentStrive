package com.Biswajeet.JobBoardApplication.Model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EmployerDetailsImplementation implements UserDetails {

    private Employer employer;
    public EmployerDetailsImplementation(Employer employer){
        this.employer=employer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("EMPLOYER"));
    }

    @Override
    public String getPassword() {
        return employer.getPassword();
    }

    @Override
    public String getUsername() {
        return employer.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
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
