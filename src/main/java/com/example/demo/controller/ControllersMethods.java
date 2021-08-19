package com.example.demo.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ControllersMethods {
    public static PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    public static Date ParseFromStringToDateTime(String date){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
//        if(date.isEmpty())
//        {
//            return dateFormat.parse(new Date());
//        }
        Date d = null;
        try {
            if (date.isEmpty())
                d = new Date();
            else
                d = dateFormat.parse(date);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Date ParseFromStringToDate(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            if (date.isEmpty())
                d = new Date();
            else
                d = dateFormat.parse(date);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
}
