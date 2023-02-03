package com.paymybuddy.transactionapp.dto;

import com.paymybuddy.transactionapp.entity.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO to create user account
 * url : "/signup"
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto implements Serializable {

    private String email;
    private String password;
    private String username;


    public CreateUserDto(UserAccount userAccount){
        this.username = userAccount.getUsername();
        this.email = userAccount.getEmail();
        this.password = userAccount.getPassword();
    }

}
