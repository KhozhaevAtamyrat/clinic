package com.example.demo.entity;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Patient {
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

    private boolean isNotBanned;

    //@Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @PastOrPresent
    private Date dob;

    private String filename;

    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
            mappedBy = "patient")
    private final List<Visit> visits = new ArrayList<>();

    public Patient() {
        this.isNotBanned = true;
    }

    public Patient(String name, String surname, String login, String password ,Date dob) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.dob = dob;
        this.isNotBanned = true;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isNotBanned() {
        return isNotBanned;
    }

    public void setNotBanned(boolean notBanned) {
        isNotBanned = notBanned;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
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

    public Long getId() {
        return id;
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
