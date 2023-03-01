package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.service.UserAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserAccountService userAccountService;

    public HomeController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    /**
     * connected user can enter this page.
     * url : "<a href="http://localhost:8080/home">...</a>"
     */

    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("userAccount", userAccountService.getConnectedUser());
        return "/home";
    }



}


