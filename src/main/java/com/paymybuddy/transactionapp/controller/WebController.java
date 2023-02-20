package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @Autowired
    UserAccountService userAccountService;

    @GetMapping("profile")
    public String profile(Model model){
        model.addAttribute("userAccount", userAccountService.getConnectedUser());
        return "profile";
    }
}
