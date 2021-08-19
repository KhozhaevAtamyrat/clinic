package com.example.demo.repos;

import com.example.demo.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepos extends JpaRepository<Admin,Long> {
    Optional<Admin> findByLogin(String login);
}
