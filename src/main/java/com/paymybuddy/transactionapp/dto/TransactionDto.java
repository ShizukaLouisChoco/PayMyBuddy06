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
import java.math.RoundingMode;

/**
 * DTO to create transaction
 * url : "/transaction"
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto implements Serializable {

    @NotNull(message = "Please select one from your contacts")
    private Long creditorId;

    @NotNull(message = "Amount cannot be empty.")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0.00.")
    private BigDecimal amount;

    @NotNull
    @NotEmpty(message = "Description cannot be empty.")
    @Size(min = 3, max = 50, message = "Description must be more than 3 letters")
    private String description;

    public BigDecimal getAmountForCreditor() {
        //fee is 0.5%
        final BigDecimal FEE = BigDecimal.valueOf(0.005);
        BigDecimal calculatedAmount = amount.subtract(amount.multiply(FEE));
        return calculatedAmount.setScale(2, RoundingMode.HALF_UP);
    }
}
