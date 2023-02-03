package com.paymybuddy.transactionapp.dto;

import com.paymybuddy.transactionapp.entity.Balance;
import com.paymybuddy.transactionapp.entity.Transaction;
import com.paymybuddy.transactionapp.entity.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceDto {

    private UUID id;

    private UserAccount user;

    private BigDecimal credit;
    private BigDecimal debit;
    private BigDecimal userBalance;
    private List<Transaction> history;

    public BalanceDto(Balance balance){
     this.id = balance.getId();
     this.user = balance.getUser();
     this.credit = balance.getCredit();
     this.debit = balance.getDebit();
     this.userBalance = balance.getUserBalance();
     this.history = balance.getHistory();
    }
}
