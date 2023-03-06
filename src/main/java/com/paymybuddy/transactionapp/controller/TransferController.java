package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.dto.TransactionDto;
import com.paymybuddy.transactionapp.entity.Transaction;
import com.paymybuddy.transactionapp.service.TransactionService;
import com.paymybuddy.transactionapp.service.UserAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/transfer")
    public String addTransaction(Model model, @Valid TransactionDto transactionDto, BindingResult result){
        //for "Select A connection", list of connections
        model.addAttribute("connections", userAccountService.getConnectedUser().getConnections());
        Page<Transaction> transactionPage = transactionService.findPaginated(PageRequest.of(0, 3));
        model.addAttribute("transactionDto", transactionDto);
        model.addAttribute("transactionPage",  transactionPage);
        //Optional<Integer>
        model.addAttribute("currentPage", 1);
        //for "My transactions" with pagenation, list of transaction(debtor)
            //display validation errors
        if(result.hasErrors()){
            return "transfer";
            /*List<String> errorList = result
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            model.addAttribute("errorMsg", errorList);*/
        }
        // Initialize transactionPage object

        //exception handling
        try {
            transactionService.createTransaction(transactionDto);
            //return "redirect:/transfer";
        }catch (Exception ex){
            model.addAttribute("connections", userAccountService.getConnectedUser().getConnections());
            model.addAttribute("transactionDto", transactionDto);
            model.addAttribute("transactionPage",  transactionPage);
            model.addAttribute("currentPage", 1);
            //add error message to model
            model.addAttribute("errorMsg", ex.getMessage());
            //return to "transfer" page
            return "transfer";
        }
        return "redirect:/transfer";

    }
}


