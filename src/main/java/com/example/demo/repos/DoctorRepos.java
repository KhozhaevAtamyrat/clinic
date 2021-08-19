package com.example.demo.repos;

import com.example.demo.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepos extends JpaRepository<Doctor,Long> {
    Optional<Doctor> findByLogin(String login);
}
