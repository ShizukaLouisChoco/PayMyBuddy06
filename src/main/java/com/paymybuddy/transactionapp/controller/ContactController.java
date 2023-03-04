package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.service.UserAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ContactController {

    UserAccountService userAccountService;

    public ContactController(UserAccountService userAccountService){
        this.userAccountService = userAccountService;
    }

    /**
     * connected user can enter this page.
     * url : "<a href="http://localhost:8080/contact">...</a>"
     */
    @GetMapping("/contact")
    public String contact(Model model){
        model.addAttribute("userAccount",userAccountService.getConnectedUser());
        model.addAttribute("email", "");
        model.addAttribute("connections",userAccountService.getConnectedUser().getConnections());
        return "contact";
    }
    /**
     * connected user can add friend in this page.
     * url : "<a href="http://localhost:8080/contact">...</a>"
     */

    @PostMapping("/contact")
    public String addContact(@ModelAttribute("email")String email){
        userAccountService.addFriend(email);
        return"redirect:/contact";
    }
}
