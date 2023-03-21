package com.paymybuddy.transactionapp.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class TransactionDtoTest {


    @Test
    @DisplayName("getAmountForCreditor returns amount for creditor ")
    public void givenTransactionDto_whenCreateTransaction_thenReturnTransaction() {
        //GIVEN
    TransactionDto transactionDto = new TransactionDto(1L, BigDecimal.valueOf(30),"test");

        //WHEN
        var result = transactionDto.getAmountForCreditor();

        //THEN
        assertThat(result)
                .isNotNull()
                .satisfies(arg ->{
                    assertThat(arg).isEqualTo(BigDecimal.valueOf(29.85));
                });

    }


    }
