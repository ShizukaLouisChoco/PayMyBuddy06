package com.paymybuddy.transactionapp.dto;

import com.paymybuddy.transactionapp.entity.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO to create transaction
 * url : "/transaction"
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    private Long creditorId;

    private BigDecimal amount;

    private String description;

}
