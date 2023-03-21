package com.paymybuddy.transactionapp.entity;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class TransactionTest {

    @Test
    @DisplayName("SetCreditAmount sets amount for creditor ")
    public void givenTransaction_whenSetCreditAmount_thenSetCreditAmount() {
        //GIVEN
        UserAccount creditor = new UserAccount(1L);
        UserAccount debtor = new UserAccount(2L);

        Transaction  transaction = new Transaction(1L,creditor, debtor,BigDecimal.valueOf(30),"test",null);

        //WHEN
        transaction.setCreditAmount();

        //THEN
        var result = transaction.getCreditAmount();
        assertThat(result)
                .isNotNull()
                .satisfies(arg ->{
                    assertThat(arg).isEqualTo(BigDecimal.valueOf(29.85));
                });

    }


}
