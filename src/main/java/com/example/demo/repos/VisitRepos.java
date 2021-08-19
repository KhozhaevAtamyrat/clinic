package com.example.demo.repos;

import com.example.demo.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitRepos extends JpaRepository<Visit,Long> {
    List<Visit> findAllByPatientId (Long id);
    List<Visit> findAllByPatient_Login(String login);
}
