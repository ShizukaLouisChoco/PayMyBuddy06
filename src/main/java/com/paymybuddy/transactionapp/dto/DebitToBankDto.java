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
    @Size(min = 3)
    @NotEmpty(message = "titulaire cannot be empty.")
    private String titulaire;

    @NotNull
    @Size(min = 23, max=23)
    @NotEmpty(message = "rib cannot be empty.")
    private String rib;

    @NotNull
    @Size(min = 27, max = 27)
    @NotEmpty(message = "iban cannot be empty.")
    private String iban;

    @NotNull
    @Size(min = 8)
    @NotEmpty(message = "swift cannot be empty.")
    private String swift;
    @NotNull(message = "Amount cannot be empty.")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0.00.")
    private BigDecimal debitAmount;

}
