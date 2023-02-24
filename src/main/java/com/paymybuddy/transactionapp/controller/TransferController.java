package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.dto.TransactionDto;
import com.paymybuddy.transactionapp.entity.Transaction;
import com.paymybuddy.transactionapp.service.TransactionService;
import com.paymybuddy.transactionapp.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class TransferController {

    @Autowired
    private final UserAccountService userAccountService;

    @Autowired
    private final TransactionService transactionService;

    public TransferController(UserAccountService userAccountService, TransactionService transactionService) {
        this.userAccountService = userAccountService;
        this.transactionService = transactionService;
    }

    /**
     * return transfer page of logged in user
     * @return
     */
    @RequestMapping(value="/transfer", method = RequestMethod.GET)
    public String transferPage(Model model,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size)  {
        //This page needs: list of connections, list of transaction (debtor)
        model.addAttribute("userAccount",userAccountService.getConnectedUser());
        //for transactionDto
        model.addAttribute("transactionDto",new TransactionDto());
        //for "Select A connection", list of connections
        model.addAttribute("connections", userAccountService.getConnectedUser().getConnections());
        //for "My transactions" with pagenation, list of transaction(debtor)
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Transaction> transactionPage = transactionService.findPaginated(PageRequest.of(currentPage -1, pageSize));
        //Page<Transaction>
        model.addAttribute("transactionPage",  transactionPage);
        //Optional<Integer>
        model.addAttribute("currentPage", page);
        //int
        model.addAttribute("totalPages", transactionPage.getTotalPages());
        //int
        model.addAttribute("pageSize",transactionPage.getSize());
        return "/transfer";
    }

    @PostMapping("/transfer")
    public String addTransaction(@Validated @ModelAttribute("transactionDTO")TransactionDto transactionDto, BindingResult result, Model model){
        if(result.hasErrors()){
            //display validation errors
            List<String> errorList = new ArrayList<String>();
            for(ObjectError error : result.getAllErrors()){
                errorList.add(error.getDefaultMessage());
            }
            model.addAttribute("ValidationError", errorList);
            return "/transfer";
        }
        //if there is no error, create new transaction
        transactionService.createTransaction(transactionDto);
        return "redirect:/transfer";

    }
}


