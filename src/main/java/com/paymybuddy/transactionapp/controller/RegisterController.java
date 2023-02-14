package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.dto.RegisterDto;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.exception.EmailAlradyExistException;
import com.paymybuddy.transactionapp.service.UserAccountService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegisterController {

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/register")
    public String signIn(WebRequest webRequest,Model model){
        RegisterDto registerDto = new RegisterDto();
        model.addAttribute("register",registerDto);
        return "register";
    }

    @PostMapping("/register")
    public String saveUserAccount(@Validated @ModelAttribute("register") RegisterDto userAccount, HttpServletRequest request, Errors errors)throws EmailAlradyExistException {
        userAccountService.createUser(userAccount);
        return "redirect:login";
    }
}
