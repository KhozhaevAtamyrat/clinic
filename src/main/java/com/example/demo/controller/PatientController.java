package com.example.demo.controller;

import com.example.demo.entity.Patient;
import com.example.demo.repos.PatientRepos;
import com.example.demo.repos.VisitRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Controller
@PreAuthorize("hasAuthority('PATIENT')")
@RequestMapping("/patient")
public class PatientController {

    private final VisitRepos visitRepos;
    private final PatientRepos patientRepos;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    public PatientController(VisitRepos visitRepos, PatientRepos patientRepos) {
        this.visitRepos = visitRepos;
        this.patientRepos = patientRepos;
    }

    @GetMapping()
    public String getPatient(Model model){
        Patient p = findPatient();

        model.addAttribute("visits",visitRepos.findAllByPatient_Login(p.getLogin()));
        return "/patient/visits";
    }

    @GetMapping("/settings")
    public String getPatientSettings(Model model){

        Patient p = findPatient();

        if(p==null) {
            return "redirect:/patient";
        }

        model.addAttribute("patient",p);
        return "/patient/profile-settings";
    }

    public Patient findPatient(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        return patientRepos.findByLogin(login).orElse(null);
    }

    @GetMapping("/profile")
    public String getPatientInfo(Model model){

        Patient p = findPatient();

        if(p==null) {
            return "redirect:/patient";
        }

        model.addAttribute("patient",p);
        return "/patient/profile";
    }


    @PostMapping("/settings")
    public String getPatientSettings(@RequestParam String name,
                                     @RequestParam String surname,
                                     @RequestParam String dob,
                                     @RequestParam MultipartFile file) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Patient p = patientRepos.findByLogin(username).orElse(null);

        if(p!= null){
            p.setName(name);
            p.setSurname(surname);
            p.setDob(ControllersMethods.ParseFromStringToDate(dob));

            if(!Objects.requireNonNull(file.getOriginalFilename()).isEmpty()){
                File uploadDir = new File(uploadPath);
                if(!uploadDir.exists()){
                    uploadDir.mkdir();
                }
                String uuidFile = UUID.randomUUID().toString();
                String resultFileName = uuidFile + "." + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + "/" + resultFileName));

                if(p.getFilename() != null){
                    File fileToDelete = new File(uploadPath + "/" + p.getFilename());
                    fileToDelete.delete();
                }
                p.setFilename(resultFileName);
            }

            patientRepos.save(p);
        }

        return "redirect:/patient/";
    }

}
