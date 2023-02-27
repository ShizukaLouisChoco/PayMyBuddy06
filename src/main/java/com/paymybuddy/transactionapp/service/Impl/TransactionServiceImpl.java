package com.paymybuddy.transactionapp.service.Impl;

import com.paymybuddy.transactionapp.dto.TransactionDto;
import com.paymybuddy.transactionapp.entity.Transaction;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.repository.TransactionRepository;
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

    private final UserAccountService userAccountService;
    private final TransactionRepository transactionRepository;


    /**
     * user make a transaction to friend with
     * url : <a href="http://localhost:8080/transaction">...</a>
     *
     * @return TransactionDto
     */
    @Override
    @Transactional
    public Transaction createTransaction(TransactionDto transaction) {
        /*
        UserAccount connectedUser = userAccountService.getConnectedUser();
        //verifiy balance is ok for transaction
        BigDecimal debitAmount = transaction.getAmountForDebtor();
        if (connectedUser.getBalance().compareTo(debitAmount) <= 0) {
            throw new BalanceException();
        }

        Transaction newTransaction = new Transaction(null, userAccountRepository.getReferenceById(transaction.getCreditorId()), connectedUser, transaction.getAmount(), transaction.getDescription(), debitAmount);
        transactionRepository.save(newTransaction);


*/

            UserAccount debtor = userAccountService.getConnectedUser();
            UserAccount creditor = userAccountService.getUserById(transaction.getCreditorId());

        BigDecimal connectedUserBalance = debtor.getBalance();
        BigDecimal newConnectedUserBalance = connectedUserBalance.subtract(transaction.getAmount());
        debtor.setBalance(newConnectedUserBalance);
        userAccountService.update(debtor);

        BigDecimal creditorBalance = creditor.getBalance();
        BigDecimal newCreditorBalance = creditorBalance.add(transaction.getAmountForCreditor());
        creditor.setBalance(newCreditorBalance);
        userAccountService.update(creditor);

        Transaction newTransaction = new Transaction(null,creditor,debtor,transaction.getAmount(),transaction.getDescription(),transaction.getAmountForCreditor());

            transactionRepository.save(newTransaction);


        return newTransaction;
    }

    @Override
    public List<Transaction> findAllByCreditorId(Long id) {
        return transactionRepository.findAllByCreditorId(id);
    }


    @Override
    public Page<Transaction> findPaginated(Pageable pageable) {
        Long debtorId = userAccountService.getConnectedUser().getId();
        return transactionRepository.findAllByDebtorId(debtorId,pageable);
    }
}

