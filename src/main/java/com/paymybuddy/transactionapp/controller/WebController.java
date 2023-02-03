package com.paymybuddy.transactionapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/index")
    public String home(Model model){
        model.addAttribute("paramValue", "PARAM VALUE");
        return "we are at home";
    }

}
