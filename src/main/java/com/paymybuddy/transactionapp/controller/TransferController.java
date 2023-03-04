package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.dto.TransactionDto;
import com.paymybuddy.transactionapp.entity.Transaction;
import com.paymybuddy.transactionapp.service.TransactionService;
import com.paymybuddy.transactionapp.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
                               @RequestParam(name = "page", defaultValue = "1" ) Integer currentPage,
                               @RequestParam(name = "size", defaultValue = "3") Integer pageSize)  {
        //This page needs: list of connections, list of transaction (debtor)
        model.addAttribute("userAccount",userAccountService.getConnectedUser());
        //for transactionDto
        model.addAttribute("transactionDto",new TransactionDto());
        //for "Select A connection", list of connections
        model.addAttribute("connections", userAccountService.getConnectedUser().getConnections());
        //for "My transactions" with pagenation, list of transaction(debtor)

        Page<Transaction> transactionPage = transactionService.findPaginated(PageRequest.of(currentPage -1, pageSize));
        //Page<Transaction>
        model.addAttribute("transactionPage",  transactionPage);
        //Optional<Integer>
        model.addAttribute("currentPage", currentPage);
        return "/transfer";
    }
    //TODO: https://www.baeldung.com/spring-thymeleaf-error-messages

    @PostMapping("/transfer")
    public String addTransaction(@Validated @ModelAttribute("transactionDTO")TransactionDto transactionDto, BindingResult result, Model model){
        if(result.hasErrors()){
            //display validation errors
            List<String> errorList = result
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            model.addAttribute("errorMsg", errorList);
            return "/transfer";
        }
        //if there is no error, create new transaction
        try {
            transactionService.createTransaction(transactionDto);
        }catch (Exception ex){
            model.addAttribute("errorMsg", ex.getMessage());
        }
        return "redirect:/transfer";

    }
}


