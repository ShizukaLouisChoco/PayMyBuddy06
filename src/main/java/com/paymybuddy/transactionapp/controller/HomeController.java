package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.service.UserAccountService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserAccountService userAccountService;

    public HomeController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }


    @GetMapping("/home")
    public String home(Authentication authentication, Model model){
        model.addAttribute("userAccount", authentication.getName());
        return "home";
    }
}


