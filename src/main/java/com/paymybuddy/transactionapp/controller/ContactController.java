package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.service.ConnectedUserDetailsService;
import com.paymybuddy.transactionapp.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ContactController {

    private final ConnectedUserDetailsService connectedUserDetailsService;
    private final UserAccountService userAccountService;


    /**
     * connected user can enter this page.
     * url : "<a href="http://localhost:8080/contact">...</a>"
     */
    @GetMapping("/contact")
    public String contactPage(Model model){
        model.addAttribute("userAccount",connectedUserDetailsService.getConnectedUser());
        model.addAttribute("email", "");
        model.addAttribute("connections",connectedUserDetailsService.getConnectedUser().getFriends());
        return "/contact";
    }
    /**
     * connected user can add friend in this page.
     * url : "<a href="http://localhost:8080/contact">...</a>"
     */

    @PostMapping("/contact")
    public String addContact(@ModelAttribute("email")String email,Model model){
        //validation error
        if(email.isEmpty()){
            model.addAttribute("userAccount",connectedUserDetailsService.getConnectedUser());
            model.addAttribute("email", "");
            model.addAttribute("connections",connectedUserDetailsService.getConnectedUser().getFriends());
            model.addAttribute("errorMsg", "Email cannot be empty");
            return "/contact";
        }
        //exception handling
        try{
        userAccountService.addFriend(email);
        }catch(Exception ex){
            model.addAttribute("userAccount",connectedUserDetailsService.getConnectedUser());
            model.addAttribute("email", "");
            model.addAttribute("connections",connectedUserDetailsService.getConnectedUser().getFriends());
            model.addAttribute("errorMsg", ex.getMessage());
            return "/contact";
        }
        return"redirect:/contact";
    }
}
