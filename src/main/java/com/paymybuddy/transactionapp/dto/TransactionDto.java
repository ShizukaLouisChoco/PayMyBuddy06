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

/**
 * DTO to create transaction
 * url : "/transaction"
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto implements Serializable {

    //creditorId
    @NotNull(message = "Please select one from your contacts")
    private Long creditorId;

    @NotNull(message = "Amount cannot be empty.")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0.00.")
    private BigDecimal amount;

    @NotNull
    @Size(min = 3)
    @NotEmpty(message = "Description cannot be empty.")
    private String description;

    public BigDecimal getAmountForDebtor() {
        BigDecimal fee = BigDecimal.valueOf(0.05);
        return amount.subtract(amount.multiply(fee));
    }
}
