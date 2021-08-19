package com.example.demo.controller;

import com.example.demo.Specialty;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.repos.AdminRepos;
import com.example.demo.repos.DoctorRepos;
import com.example.demo.repos.PatientRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController {
    private final DoctorRepos doctorRepos;
    private final PatientRepos patientRepos;
    private final AdminRepos adminRepos;

    @Autowired
    public AdminController(DoctorRepos doctorRepos, PatientRepos patientRepos, AdminRepos adminRepos) {
        this.doctorRepos = doctorRepos;
        this.patientRepos = patientRepos;
        this.adminRepos = adminRepos;
    }

    @GetMapping("/doctors")
    public String getAllDoctors(Model model){
        model.addAttribute("doctors",doctorRepos.findAll());
        return "/admin/all-doctors";
    }

    @PostMapping("/doctors/delete/{id}")
    public String deleteDoctor(@PathVariable Long id){
        doctorRepos.deleteById(id);
        return "redirect:/admin/doctors";
    }

    @GetMapping("/doctors/edit/{id}")
    public String getEditDoctorPage(@PathVariable Long id, Model model){
        Doctor doctor = doctorRepos.findById(id).orElse(null);
        if(doctor==null)
            return "redirect:/admin/doctors";

        model.addAttribute("doctor",doctor);

        return "/admin/edit-doctor";
    }

    @PostMapping("/doctors/edit/{id}")
    public String editDoctor(@PathVariable Long id, @RequestParam String name,
                             @RequestParam String surname,
                             @RequestParam String login, @RequestParam String password,
                             @RequestParam String patronymic, @RequestParam Specialty specialty){

        Doctor doctor = doctorRepos.findById(id).orElse(null);
        if(doctor!=null) {
            if(password!=null && !password.isEmpty()){
                doctor.setPassword(ControllersMethods.getPasswordEncoder().encode(password));
            }
            doctor.setName(name);
            doctor.setSurname(surname);
            doctor.setLogin(login);
            doctor.setPatronymic(patronymic);
            doctor.setSpecialty(specialty);

            doctorRepos.save(doctor);
        }

        return "redirect:/admin/doctors";
    }

    @GetMapping("/addDoctor")
    public String addDoctorPage(Model model){
        model.addAttribute("doctor",new Doctor());
        return "/admin/add-doctor";
    }

    @PostMapping("/doctors")
    public String addDoctor(@ModelAttribute("person") @Valid Doctor doctor, BindingResult bindingResult,
                            Model model){

        if(bindingResult.hasErrors()) {
            return "/admin/add-doctor";
        }

        String login = doctor.getLogin();

        if(doctorRepos.findByLogin(login).isPresent() || patientRepos.findByLogin(login).isPresent()
                || adminRepos.findByLogin(login).isPresent()){
            model.addAttribute("message","Пользователь с таким логином уже существует!");
            model.addAttribute("doctor",doctor);
            return "/admin/add-doctor";
        }

        doctor.setPassword(ControllersMethods.getPasswordEncoder().encode(doctor.getPassword()));
        doctorRepos.save(doctor);

        return "redirect:/admin/doctors";
    }

    @GetMapping("/patients")
    public String getAllPatients(Model model){
        model.addAttribute("patients",patientRepos.findAll());
        return "/admin/all-patients";
    }

    @GetMapping("/patients/status/{id}")
    public String banPatient(@PathVariable Long id){
        Patient patient = patientRepos.findById(id).orElse(null);

        if(patient!=null) {
            patient.setNotBanned(!patient.isNotBanned());
            patientRepos.save(patient);
        }

        return "redirect:/admin/patients";
    }



}
