package com.paymybuddy.transactionapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitToBankDto implements Serializable {


    @NotNull
    @Size(min = 3, message = "Titrlaire must be more than 3 letters")
    @NotEmpty(message = "Titulaire cannot be empty.")
    private String titulaire;

    @NotNull
    @Size(min = 23, max=23, message = "RIB must be more than 23 numbers")
    @NotEmpty(message = "RIB cannot be empty.")
    private String rib;

    @NotNull
    @Size(min = 27, max = 27, message = "IBAN must be 27 numbers")
    @NotEmpty(message = "IBAN cannot be empty.")
    private String iban;

    @NotNull
    @Size(min = 8, message = "SWIFT must be more than 8 letters")
    @NotEmpty(message = "SWIFT cannot be empty.")
    private String swift;
    @NotNull(message = "Amount cannot be empty.")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0.00.")
    private BigDecimal debitAmount;

}
