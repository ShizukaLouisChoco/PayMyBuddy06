package com.paymybuddy.transactionapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditToBankDto implements Serializable {

    @NotNull(message = "Amount cannot be empty.")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0.00.")
    private BigDecimal creditAmount;
}
