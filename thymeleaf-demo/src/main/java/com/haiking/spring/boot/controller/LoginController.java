package com.haiking.spring.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;

@Controller
public class LoginController {

    public  String toLogin(ModelAndView model){
        model.addObject("currentYear", Calendar.getInstance().getWeekYear());
        return  "login";
    }
}
