package com.example.demo;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

public enum Role {
    PATIENT,
    DOCTOR,
    ADMIN;

    public Set<SimpleGrantedAuthority> getAuthorities(){
        Set<SimpleGrantedAuthority> authorities = new HashSet<SimpleGrantedAuthority>();
        String name = this.name();

        authorities.add(new SimpleGrantedAuthority(name));
        return authorities;
    }
}
