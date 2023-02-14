package com.paymybuddy.transactionapp.service;

import com.paymybuddy.transactionapp.dto.TransactionDto;
import com.paymybuddy.transactionapp.entity.Transaction;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.repository.TransactionRepository;
import com.paymybuddy.transactionapp.service.Impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    private TransactionService transactionService;

    @BeforeEach
    public void setup() {
        this.transactionService = new TransactionServiceImpl(transactionRepository);

    }

    @Test
    @DisplayName("TransactionService calls transactionRepository addTransaction method")
    public void TestCreateTransaction(){
        // GIVEN
        UserAccount creditorAccount = new UserAccount(UUID.randomUUID(), "creditor@email.com","creditor1","pass123",null,null);
        TransactionDto transactionDto = new TransactionDto(creditorAccount, BigDecimal.valueOf(100),"test create transaction");
        // WHEN
        var result = transactionService.createTransaction(transactionDto);

        // THEN
        verify(transactionRepository,times(1)).save(transactionDto);
        assertThat(result).isEqualTo(transactionDto);
    }
    @Test
    @DisplayName("TransactionService calls transactionRepository findByCreditor method ")
    public void TestFindByCreditor(){
        // GIVEN
        UserAccount creditorAccount = new UserAccount(UUID.randomUUID(), "creditor@email.com","creditor1","pass123",null,null);
        UserAccount debtorAccount = new UserAccount(UUID.randomUUID(), "debtor@email.com","debtor1","pass123",null,null);
        List<Transaction> expectedResult = List.of(new Transaction(UUID.randomUUID(),creditorAccount,debtorAccount,BigDecimal.valueOf(100),"test",BigDecimal.valueOf(3)));
        // WHEN
        when(transactionRepository.findByCreditor(creditorAccount.getId())).thenReturn(expectedResult);
        var result = transactionService.findByCreditor(creditorAccount.getId());

        // THEN
        verify(transactionRepository,times(1)).findByCreditor(creditorAccount.getId());
        assertThat(result).isEqualTo(expectedResult);
    }
    @Test
    @DisplayName("TransactionService calls transactionRepository findByDebtor method ")
    public void TestFindByDebtor(){
        // GIVEN
        UserAccount debtorAccount = new UserAccount(UUID.randomUUID(), "debtor@email.com","debtor1","pass123",null,null);
        // WHEN
        var result = transactionService.findByCreditor(debtorAccount.getId());
        // THEN
        verify(transactionRepository,times(1)).findByCreditor(debtorAccount.getId());

    }

/*
*  UserAccount userAccountCreditor = new UserAccount(UUID.randomUUID(),"creditor@email","creditor","newPassword",null,null);
        UserAccount userAccountDebtor = new UserAccount(UUID.randomUUID(),"debtor@email","debtor","newPassword",null,null);
        Balance balance = new Balance(UUID.randomUUID(),userAccountCreditor,null,null,null,null);
        Transaction transaction1 = new Transaction(UUID.randomUUID(),userAccountCreditor,userAccountDebtor,BigDecimal.valueOf(100),"repas1",BigDecimal.valueOf(0.05));
        Transaction transaction2 = new Transaction(UUID.randomUUID(),userAccountCreditor,userAccountDebtor,BigDecimal.valueOf(50),"repas2",BigDecimal.valueOf(0.05));
        List<Transaction> history = new ArrayList<>();
        history.add(transaction1);
        history.add(transaction2);
        userAccountCreditor.setBalance(balance);
        BigDecimal amount = BigDecimal.valueOf(50);*/

}
