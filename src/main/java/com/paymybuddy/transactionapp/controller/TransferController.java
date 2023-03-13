package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.dto.TransactionDto;
import com.paymybuddy.transactionapp.entity.Transaction;
import com.paymybuddy.transactionapp.service.ConnectedUserDetailsService;
import com.paymybuddy.transactionapp.service.TransactionService;
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
    private final ConnectedUserDetailsService connectedUserDetailsService;

    @Autowired
    private final TransactionService transactionService;

    public TransferController(ConnectedUserDetailsService connectedUserDetailsService, TransactionService transactionService) {
        this.connectedUserDetailsService = connectedUserDetailsService;
        this.transactionService = transactionService;    }

    /**
     * return transfer page of logged in user
     * @return
     */
    @RequestMapping(value="/transfer", method = RequestMethod.GET)
    public String transferPage(Model model,
                               @RequestParam(name = "page", defaultValue = "1" ) Integer currentPage,
                               @RequestParam(name = "size", defaultValue = "3") Integer pageSize)  {
        model.addAttribute("userAccount",connectedUserDetailsService.getConnectedUser());
        model.addAttribute("transactionDto",new TransactionDto());
        model.addAttribute("connections", connectedUserDetailsService.getConnectedUser().getFriends());
        Page<Transaction> transactionPage = transactionService.findPaginated(PageRequest.of(currentPage -1, pageSize));
        model.addAttribute("transactionPage",  transactionPage);
        model.addAttribute("currentPage", currentPage);
        return "/transfer";
    }

    @PostMapping("/transfer")
    public String addTransaction(Model model, @Valid TransactionDto transactionDto, BindingResult result){
        model.addAttribute("connections", connectedUserDetailsService.getConnectedUser().getFriends());
        Page<Transaction> transactionPage = transactionService.findPaginated(PageRequest.of(0, 3));
        model.addAttribute("transactionDto", transactionDto);
        model.addAttribute("transactionPage",  transactionPage);
        model.addAttribute("currentPage", 1);
            //validation errors
        if(result.hasErrors()){
            return "transfer";
        }

        //exception handling
        try {
            transactionService.createTransaction(transactionDto);
            //return "redirect:/transfer";
        }catch (Exception ex){
            model.addAttribute("connections", connectedUserDetailsService.getConnectedUser().getFriends());
            model.addAttribute("transactionDto", transactionDto);
            model.addAttribute("transactionPage",  transactionPage);
            model.addAttribute("currentPage", 1);
            model.addAttribute("errorMsg", ex.getMessage());
            return "transfer";
        }
        return "redirect:/transfer";

    }
}


