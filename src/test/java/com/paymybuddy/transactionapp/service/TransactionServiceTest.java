package com.paymybuddy.transactionapp.service;

import com.paymybuddy.transactionapp.dto.TransactionDto;
import com.paymybuddy.transactionapp.entity.Transaction;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.repository.TransactionRepository;
import com.paymybuddy.transactionapp.service.Impl.TransactionServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    TransactionServiceImpl transactionService;
    @Mock
    ConnectedUserDetailsService connectedUserDetailsService;
    @Mock
    UserAccountService userAccountService;
    @Mock
    TransactionRepository transactionRepository;


    @Test
    @DisplayName("TransactionService calls transactionRepository addTransaction method")
    public void givenTransactionDto_whenCreateTransaction_thenReturnTransaction() {
        // GIVEN
        UserAccount debtorAccount = new UserAccount(1L, "debtor@email.com", "debtor1", "pass123", BigDecimal.valueOf(200), null);
        UserAccount creditorAccount = new UserAccount(2L, "creditor@email.com", "creditor1", "pass123", BigDecimal.ZERO, null);
        TransactionDto transactionDto = new TransactionDto(creditorAccount.getId(), BigDecimal.valueOf(100), "test create transaction");

        when(connectedUserDetailsService.getConnectedUser()).thenReturn(debtorAccount);
        when(userAccountService.getUserById(creditorAccount.getId())).thenReturn(creditorAccount);

        // WHEN
        var result = transactionService.createTransaction(transactionDto);

        // THEN
        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(1)).save(transactionArgumentCaptor.capture());

        assertThat(transactionArgumentCaptor.getValue())
                .isNotNull()
                .satisfies(arg -> {
                    assertThat(arg.getDebtor().getId()).isEqualTo(debtorAccount.getId());
                });

    }
    @Test
    @DisplayName("TransactionService calls transactionRepository findByCreditor method ")
    public void TestFindByCreditor(){
        // GIVEN
        UserAccount creditorAccount = new UserAccount(1L, "creditor@email.com","creditor1","pass123",null,null);
        UserAccount debtorAccount = new UserAccount(2L, "debtor@email.com","debtor1","pass123",null,null);
        List<Transaction> expectedResult = List.of(new Transaction(100L,creditorAccount,debtorAccount,BigDecimal.valueOf(100),"test",BigDecimal.valueOf(3)));

        // WHEN
        when(transactionRepository.findAllByCreditorId(creditorAccount.getId())).thenReturn(expectedResult);
        var result = transactionService.findAllByCreditorId(creditorAccount.getId());

        // THEN
        verify(transactionRepository,times(1)).findAllByCreditorId(creditorAccount.getId());
        assertThat(result).isEqualTo(expectedResult);
    }
    @Test
    @DisplayName("TransactionService calls transactionRepository findByDebtor method ")
    public void TestFindByDebtor(){
        // GIVEN
        UserAccount debtorAccount = new UserAccount(1L, "debtor@email.com","debtor1","pass123",null,null);
        // WHEN
        var result = transactionService.findAllByCreditorId(debtorAccount.getId());
        // THEN
        verify(transactionRepository,times(1)).findAllByCreditorId(debtorAccount.getId());

    }
    @Test
    @DisplayName("TransactionService calls transactionRepository findByDebtor method ")
    public void paginatedTest(){
        // GIVEN

        UserAccount creditorUserAccount = new UserAccount(2L, "creditor@email.com","creditor","creditor",null,null);
        UserAccount connectedUserAccount = new UserAccount(1L, "debtor@email.com","debtor","debtor",null, List.of(creditorUserAccount));
        Transaction transaction1 = new Transaction(new TransactionDto(creditorUserAccount.getId(),BigDecimal.valueOf(15),"transaction1"),connectedUserAccount.getId());
        Transaction transaction2 = new Transaction(new TransactionDto(creditorUserAccount.getId(),BigDecimal.valueOf(15),"transaction2"),connectedUserAccount.getId());
        List<Transaction> transactionList = new ArrayList<Transaction>();
        transactionList.add(transaction1);
        transactionList.add(transaction2);
        int pageSize = 2;
        int pageNumber = 0;
        Sort sort = Sort.by(Sort.Direction.DESC, "transactionId"); // ソート条件
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Transaction> expectedResult = new PageImpl<Transaction>(transactionList, pageable, transactionList.size());

        // WHEN
        when(connectedUserDetailsService.getConnectedUser()).thenReturn(connectedUserAccount);
        when(transactionRepository.findAllByDebtorId(connectedUserAccount.getId(),pageable)).thenReturn(expectedResult);
        var result = transactionService.findPaginated(pageable);
        // THEN
        verify(transactionRepository,times(1)).findAllByDebtorId(connectedUserAccount.getId(),pageable);
        assertThat(result).isEqualTo(expectedResult);
    }
}
