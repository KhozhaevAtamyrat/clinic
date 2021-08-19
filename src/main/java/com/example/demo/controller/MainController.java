package com.example.demo.controller;

import com.example.demo.Role;
import com.example.demo.entity.Patient;
import com.example.demo.repos.AdminRepos;
import com.example.demo.repos.DoctorRepos;
import com.example.demo.repos.PatientRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MainController {

    private final DoctorRepos doctorRepos;
    private final PatientRepos patientRepos;
    private final AdminRepos adminRepos;

    public MainController(DoctorRepos doctorRepos, PatientRepos patientRepos, AdminRepos adminRepos) {
        this.doctorRepos = doctorRepos;
        this.patientRepos = patientRepos;
        this.adminRepos = adminRepos;
    }

    @RequestMapping(path = "/success")
    public String getDefaultPage(){
        Collection<SimpleGrantedAuthority> authorities =
                (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if(authorities.containsAll(Role.DOCTOR.getAuthorities())){
            return "redirect:/doctor/patients";
        }
        else if(authorities.containsAll(Role.PATIENT.getAuthorities())) {
            return "redirect:/patient";
        }
        else if(authorities.containsAll(Role.ADMIN.getAuthorities())) {
            return "redirect:/admin/doctors";
        }
        else
            return "redirect:/";
    }

    @RequestMapping(path = "/aboutApp")
    @GetMapping
    public String getAboutAppPage(){
        return "/main/about-app";
    }

    @RequestMapping(path = "/aboutMe")
    @GetMapping
    public String getAboutMePage(){
        return "/main/about-me";
    }

    @RequestMapping(path = "/")
    @GetMapping
    public String getLoginPage(){
        return "/main/login";
    }


    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String getRegPage(Model model){
        model.addAttribute("patient",new Patient());
        return "/main/registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String addUser(@ModelAttribute("patient") Patient patient,
                          Model model){
/*@RequestParam String login,
                          @RequestParam String password,
                          @RequestParam String name,
                          @RequestParam String surname,
                          @RequestParam String dob*/

        String login = patient.getLogin();
        if(doctorRepos.findByLogin(login).isPresent() ||
                patientRepos.findByLogin(login).isPresent() || adminRepos.findByLogin(login).isPresent()){
            model.addAttribute("patient",patient);
            model.addAttribute("message","Пользователь с таким логином уже существует!");
            return "main/registration";
        }

        patient.setPassword(ControllersMethods.getPasswordEncoder().encode(patient.getPassword()));

        patientRepos.save(patient);
        return "/main/login";
    }



}
