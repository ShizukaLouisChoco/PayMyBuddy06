package com.paymybuddy.transactionapp.service.Impl;

import com.paymybuddy.transactionapp.dto.TransactionDto;
import com.paymybuddy.transactionapp.entity.Transaction;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.repository.TransactionRepository;
import com.paymybuddy.transactionapp.service.ConnectedUserDetailsService;
import com.paymybuddy.transactionapp.service.TransactionService;
import com.paymybuddy.transactionapp.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final static BigDecimal FEE = BigDecimal.valueOf(0.995);

    private final ConnectedUserDetailsService connectedUserDetailsService;
    private final UserAccountService userAccountService;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public Transaction createTransaction(TransactionDto transaction) {
        UserAccount debtor = connectedUserDetailsService.getConnectedUser();
        UserAccount creditor = userAccountService.getUserById(transaction.getCreditorId());

        debtor.debitAmount(transaction.getAmount());
        creditor.creditAmount(transaction.getAmount().multiply(FEE));

        userAccountService.update(debtor);
        userAccountService.update(creditor);

        Transaction newTransaction = new Transaction(transaction,debtor.getId());
        transactionRepository.save(newTransaction);


        return newTransaction;
    }

    @Override
    public List<Transaction> findAllByCreditorId(Long id) {
        return transactionRepository.findAllByCreditorId(id);
    }


    @Override
    public Page<Transaction> findPaginated(Pageable pageable) {
        Long debtorId = connectedUserDetailsService.getConnectedUser().getId();
        return transactionRepository.findAllByDebtorId(debtorId,pageable);
    }
}

