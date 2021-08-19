package com.example.demo.entity;

import com.example.demo.Specialty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min=3,max = 30,message = "Name should have 3-30 characters")
    private String name;

    @Size(min=3,max = 30,message = "Surname should have 3-30 characters")
    private String surname;

    @Size(min=3,max = 255,message = "Login should have 3-255 characters")
    private String login;

    @Size(min=3,max = 255,message = "Password should have 3-255 characters")
    private String password;

    @Size(min=3,max = 30, message = "Patronymic should have 6-30 characters")
    private String patronymic;

    @Enumerated(EnumType.STRING)
    private Specialty specialty;

    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
            mappedBy = "doctor")
    private final List<Visit> visits = new ArrayList<>();

    public Doctor() {
    }

    public Doctor(String name, String surname, String login, String password,
                  Specialty specialty, String patronymic) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.specialty = specialty;
        this.patronymic = patronymic;
    }


    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty speciality) {
        this.specialty = speciality;
    }

    public Long getId() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void addVisit(Visit visit) {
        visits.add(visit);
    }
}
