package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.service.UserAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {

    private final UserAccountService userAccountService;

    public WebController(UserAccountService userAccountService) {
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

    @GetMapping("profile")
    public String profile(Model model){
        model.addAttribute("userAccount", userAccountService.getConnectedUser());
        return "profile";
    }

    @GetMapping("/contact")
    public String contact(Model model){
        model.addAttribute("userAccount",userAccountService.getConnectedUser());
        model.addAttribute("email",new String());
        model.addAttribute("connections",userAccountService.getConnectedUser().getConnections());
        return "contact";
    }

    @PostMapping("/contact")
    public String addContact(@ModelAttribute("email")String email){
        userAccountService.addFriend(email);
        return"redirect:/contact";
    }

}


