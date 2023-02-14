package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.entity.Transaction;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.service.TransactionService;
import com.paymybuddy.transactionapp.service.UserAccountService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

public class TransferController {

    private final UserAccountService userAccountService;

    private final TransactionService transactionService;

    public TransferController(UserAccountService userAccountService, TransactionService transactionService) {
        this.userAccountService = userAccountService;
        this.transactionService = transactionService;
    }

    /**
     * return transfer page
     * @return
     */
    @GetMapping("/transfer")
    public String transferPage(Model model, @RequestParam(defaultValue ="1") int page) {
      //This page needs: list of connections, list of transaction (debtor)
        UserAccount loggedInUserAccount = userAccountService.getConnectedUser();
        //for "Select A connection", list of connections
        List<UserAccount> connections = loggedInUserAccount.getConnections();
        model.addAttribute("connections", connections);
       //for "My transactions" with pagenation, list of transaction(debtor)
       Page<Transaction> transactionPage = transactionService.findTransactionPage(loggedInUserAccount);
          model.addAttribute("transactionPage", transactionPage);
          model.addAttribute("transfers", transactionPage.getContent());
        return "transfer";
    }

    @PostMapping("/transfer")
    public String addTransaction(Authentication authentication, @RequestParam(value="username") String creditorUsername, @RequestParam(value = "amount") BigDecimal amount, @RequestParam(value="description")String description){
        //for add new transaction, it needs transactionDTO(creditor, amount, description & loggedinUserAccount(debtor)
//        ModelAndView modelAndView = new ModelAndView();
//        UserAccount debtor = userAccountService.getUser(authentication);
//        UserAccount creditor = userAccountService.getCreditorFromConnectionListByCreditorUsername(creditorUsername,debtor);
//        TransactionDto transactionDto = new TransactionDto(creditor,amount, description);
//        Transaction transaction = transactionService.createTransactionFromTransactionDto(transactionDto,debtor);
//
//            debtor.getBalance().setUserBalance(debtor.getBalance().getUserBalance().subtract(transaction.getTotalAmount()));
//            debtor.getBalance().setUserBalance(debtor.getBalance().getUserBalance().add(transaction.getAmount()));
//
//            UserAccount admin = userAccountService.findUserAccountByEmail("admin@paymybuddy");
//            admin.getBalance().setUserBalance(admin.getBalance().getUserBalance().add(transaction.getFee()));
//
//            userAccountService.updateUserAccount(debtor);
//            userAccountService.updateUserAccount(creditor);
//            userAccountService.updateUserAccount(admin);

        return "myPage";
    }
}


