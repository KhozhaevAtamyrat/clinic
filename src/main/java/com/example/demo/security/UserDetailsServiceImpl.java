package com.example.demo.security;

import com.example.demo.entity.Admin;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.repos.AdminRepos;
import com.example.demo.repos.DoctorRepos;
import com.example.demo.repos.PatientRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PatientRepos patientRepos;
    private final DoctorRepos doctorRepos;
    private final AdminRepos adminRepos;

    @Autowired
    public UserDetailsServiceImpl(PatientRepos patientRepos, DoctorRepos doctorRepos, AdminRepos adminRepos) {
        this.patientRepos = patientRepos;
        this.doctorRepos = doctorRepos;
        this.adminRepos = adminRepos;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Doctor doctor = doctorRepos.findByLogin(login).orElse(null);
        if(doctor != null){
            return SecurityUser.fromDoctorToUser(doctor);
        }
        Patient patient = patientRepos.findByLogin(login).orElse(null);
        if(patient != null){
            return SecurityUser.fromPatientToUser(patient);
        }
        Admin admin = adminRepos.findByLogin(login).orElse(null);
        if(admin != null){
            return SecurityUser.fromAdminToUser(admin);
        }
        throw new UsernameNotFoundException(String.format("User with login %s does not exists", login));
    }
}
