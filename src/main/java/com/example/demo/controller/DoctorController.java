package com.example.demo.controller;

import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Visit;
import com.example.demo.repos.DoctorRepos;
import com.example.demo.repos.PatientRepos;
import com.example.demo.repos.VisitRepos;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Controller
@PreAuthorize("hasAuthority('DOCTOR')")
@RequestMapping("/doctor")
public class DoctorController {
    private final DoctorRepos doctorRepos;
    private final PatientRepos patientRepos;
    private final VisitRepos visitRepos;

    public DoctorController(DoctorRepos doctorRepos, PatientRepos patientRepos, VisitRepos visitRepos) {
        this.doctorRepos = doctorRepos;
        this.patientRepos = patientRepos;
        this.visitRepos = visitRepos;
    }


    @GetMapping("/visits")
    public String getVisits(Model model){
        model.addAttribute("visits",visitRepos.findAll());
        return "visits";
    }

    @GetMapping("/visits/{id}")
    public String getVisits(@PathVariable Long id, Model model){
        model.addAttribute("visits",visitRepos.findAllByPatientId(id));
        Patient patient = patientRepos.findById(id).orElse(null);
        model.addAttribute("patientName", Objects.requireNonNull(patient).getName() + " " +
                Objects.requireNonNull(patient).getSurname());
        model.addAttribute("patientId",patient.getId());
        return "doctor/visits-of-patient";
    }

    @PostMapping("/visits")
    public String addVisit(@RequestParam(value = "description") String description,
                           @RequestParam(value = "date") String date,
                           @RequestParam(value = "patientId", required=false) Long patientId,
                           Model model){

        if(patientId == null) {
            model.addAttribute("visits", visitRepos.findAll());
            model.addAttribute("isAdded",false);
            return "visits";
        }


        Date d = ControllersMethods.ParseFromStringToDateTime(date);

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd'T'HH:mm");
//        Date d = null;
//        try {
//            if (date.isEmpty())
//                d = new Date();
//            else
//                d = dateFormat.parse(date);
//        }catch (ParseException e) {
//            System.out.println("AHAHAH");
//        }

        Patient patient = patientRepos.findById(patientId).orElse(null);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        Doctor doctor = doctorRepos.findByLogin(login).orElse(null);


        if(doctor != null && patient != null) {
            visitRepos.save(new Visit(description, d, patient, doctor));
            model.addAttribute("isAdded",true);
        }else{
            model.addAttribute("isAdded",false);
        }


        model.addAttribute("visits",visitRepos.findAll());
        return "visits";
    }

    @GetMapping("/patients")
    public String getPatients(@RequestParam(name="name", required = false)String name,
                              @RequestParam(name="surname", required = false)String surname,
                              @RequestParam(name="dob", required = false)String dob,
                              Model model){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if((name!= null && surname!=null)  && (!name.isEmpty() && !surname.isEmpty()) ){
            if(dob!=null && !dob.isEmpty()){
//                Date dateOfBirth = null;
//                try {
//                    dateOfBirth = simpleDateFormat.parse(dob);
//                }catch (ParseException e){
//                    e.printStackTrace();
//                    return "/doctor/all-patients";
//                }

                Date dateOfBirth = ControllersMethods.ParseFromStringToDateTime(dob);

                model.addAttribute("patients",patientRepos.findAllByNameAndSurnameAndDob(name,surname,dateOfBirth));
                model.addAttribute("dob", dateOfBirth);
                model.addAttribute("name", name);
                model.addAttribute("surname", surname);
                return "/doctor/all-patients";
            }

            model.addAttribute("patients",patientRepos.findAllByNameAndSurname(name,surname));
            model.addAttribute("name", name);
            model.addAttribute("surname", surname);
            return "/doctor/all-patients";
        }

        model.addAttribute("patients",patientRepos.findAll());
        return "/doctor/all-patients";
    }

    @GetMapping("/addVisit/{id}")
    public String addVisitPage(@PathVariable Long id, Model model){
        model.addAttribute("patient",patientRepos.findById(id).orElse(null));
        return "/doctor/new-visit";
    }

    @PostMapping("/addVisit/{id}")
    public String addVisit(@PathVariable Long id,
                           @RequestParam String date,
                           @RequestParam String description){

        Date d = null;
        if(date.isEmpty())
            d = new Date();
        else
            d = ControllersMethods.ParseFromStringToDateTime(date);

        Patient p = patientRepos.findById(id).orElse(null);

        if(p==null)
            return String.format("redirect:/addVisit/%s", id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        Doctor doctor = doctorRepos.findByLogin(login).orElse(null);

        visitRepos.save(new Visit(description,d,p,doctor));
        return String.format("redirect:/doctor/visits/%s",id);
    }

}
