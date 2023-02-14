package com.paymybuddy.transactionapp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class RegisterDto implements Serializable {

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String username;

}
