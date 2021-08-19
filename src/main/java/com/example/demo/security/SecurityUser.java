package com.example.demo.security;

import com.example.demo.Role;
import com.example.demo.entity.Admin;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Data
public class SecurityUser implements UserDetails {

    private final String login;
    private final String password;
    private final Set<SimpleGrantedAuthority> authorities;

    public SecurityUser(String login, String password, Set<SimpleGrantedAuthority> authorities) {
        this.login = login;
        this.password = password;
        this.authorities = authorities;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
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

    public static UserDetails fromDoctorToUser(Doctor doctor){
        return new User(
                doctor.getLogin(),doctor.getPassword(),
                true,true,true,true,
                Role.DOCTOR.getAuthorities()
        );
    }

    public static UserDetails fromPatientToUser(Patient patient){
        return new User(
                patient.getLogin(),patient.getPassword(),
                true,true,true, patient.isNotBanned(),
                Role.PATIENT.getAuthorities()
        );
    }

    public static UserDetails fromAdminToUser(Admin admin){
        return new User(
                admin.getLogin(),admin.getPassword(),
                true,true,true,true,
                Role.ADMIN.getAuthorities()
        );
    }

}
