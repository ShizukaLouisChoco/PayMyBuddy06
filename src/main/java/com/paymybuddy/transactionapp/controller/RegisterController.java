package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.dto.RegisterDto;
import com.paymybuddy.transactionapp.exception.EmailAlradyExistException;
import com.paymybuddy.transactionapp.service.UserAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("registerDto",new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String saveUserAccount(@Valid @ModelAttribute("registerDto") RegisterDto userAccount, BindingResult result)throws EmailAlradyExistException {
        if(result.hasErrors()){
            return "register";
        }
        userAccountService.createUser(userAccount);
        //todo:emailalreadyexistになった時に、エラーメッセージとregisterpageに戻るor エラーページに。
        return "redirect:/login";
    }
}
