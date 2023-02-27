package com.paymybuddy.transactionapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    /**
     * login page needs user's email and password
     * url : "<a href="http://localhost:8080/login">...</a>"
     * @return UserAccountServiceDto
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

}
