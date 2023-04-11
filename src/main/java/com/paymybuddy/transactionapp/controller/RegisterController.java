package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.dto.RegisterDto;
import com.paymybuddy.transactionapp.exception.EmailAlreadyExistException;
import com.paymybuddy.transactionapp.service.UserAccountService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class RegisterController {

    @Autowired
    private UserAccountService userAccountService;

    /**
     * register page can display reigister form
     * url : "<a href="http://localhost:8080/register">...</a>"
     */

    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("registerDto",new RegisterDto());
        return "register";
    }

    /**
     * register page can add user account
     * url : "<a href="http://localhost:8080/register">...</a>"
     */
    @PostMapping("/register")
    public String saveUserAccount(Model model, @Valid @ModelAttribute("registerDto") RegisterDto userAccount, BindingResult result) throws EmailAlreadyExistException {
        //validation error
        if(result.hasErrors()){
                return "register";
        }
        //exception handling
        try {
            userAccountService.createUser(userAccount);
        }catch(Exception exception){
            log.error(String.valueOf(exception));
            model.addAttribute("errorMsg" , exception.getMessage());
            return "register";
        }


        return "redirect:/login";
    }
}
