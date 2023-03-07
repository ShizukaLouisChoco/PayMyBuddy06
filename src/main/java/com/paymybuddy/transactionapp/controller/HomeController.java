package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.service.ConnectedUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ConnectedUserDetailsService connectedUserDetailsService;
    /**
     * connected user can enter this page.
     * url : "<a href="http://localhost:8080/home">...</a>"
     */

    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("userAccount", connectedUserDetailsService.getConnectedUser());
        return "/home";
    }



}


