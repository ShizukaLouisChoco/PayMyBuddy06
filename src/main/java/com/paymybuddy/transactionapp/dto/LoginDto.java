package com.paymybuddy.transactionapp.dto;

import com.paymybuddy.transactionapp.entity.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO to login
 * url : "/login"
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    private String email;
    private String password;


    public LoginDto(UserAccount userAccount){
        this.email = userAccount.getEmail();
        this.password = userAccount.getPassword();
    }
}
