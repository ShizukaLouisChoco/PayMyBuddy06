package com.paymybuddy.transactionapp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(min=7, max=320)
    @NotEmpty(message = "User's email cannot be empty.")
    private String email;

    @NotNull
    @Size(min=3)
    @NotEmpty(message = "User's password cannot be empty.")
    private String password;

    @NotNull
    @Size(min=3, max = 250)
    @NotEmpty(message = "User's username cannot be empty.")
    private String username;

}
