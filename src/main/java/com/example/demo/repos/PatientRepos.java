package com.example.demo.repos;

import com.example.demo.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PatientRepos extends JpaRepository<Patient,Long> {
    Optional<Patient> findByLogin(String login);
    Optional<Patient> findById(Long id);
    List<Patient> findAllByNameAndSurname(String name, String surname);
    List<Patient> findAllByNameAndSurnameAndDob(String name, String surname, Date dob);

}
