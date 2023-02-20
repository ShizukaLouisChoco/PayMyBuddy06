package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.service.UserAccountService;
import jakarta.websocket.server.PathParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    private final UserAccountService userAccountService;

    public HomeController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    /**
     * logged in user can enter this page.
     * url : "<a href="http://localhost:8080/login">...</a>"
     * @return UserAccountServiceDto
     */

    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("userAccount", userAccountService.getConnectedUser());
        return "home";
    }

    @GetMapping("/contact")
    public String contact(Model model){
        model.addAttribute("userAccount",userAccountService.getConnectedUser());
        model.addAttribute("email",new String());
        return "contact";
    }

    @PostMapping("/contact")
    public String addContact(@ModelAttribute("email")String email){
        userAccountService.addFriend(email);
        return"redirect:/contact";
    }
}


