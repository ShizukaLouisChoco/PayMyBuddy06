package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.dto.CreditToBankDto;
import com.paymybuddy.transactionapp.dto.DebitToBankDto;
import com.paymybuddy.transactionapp.exception.BalanceException;
import com.paymybuddy.transactionapp.service.UserAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileController {


    private final UserAccountService userAccountService;

    public ProfileController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @RequestMapping(value="/profile", method = RequestMethod.GET)
    public String profile(Model model){
        model.addAttribute("userAccount", userAccountService.getConnectedUser());
        model.addAttribute("transferDTO", new DebitToBankDto());
        model.addAttribute("creditAmount",BigDecimal.ZERO);
        model.addAttribute("creditAmountDTO", new CreditToBankDto());
        return "/profile";
    }

    @PostMapping("/profile/debit")
    public String DebitToBank(@Validated @ModelAttribute("transferDTO") DebitToBankDto transferToBank, BindingResult result, Model model){
        if(result.hasErrors()){
            //display validation errors
            List<String> errorList = new ArrayList<String>();
            for(ObjectError error : result.getAllErrors()){
                errorList.add(error.getDefaultMessage());
            }
            model.addAttribute("ValidationError", errorList);
            return "/profile";
        }

           userAccountService.debitBalance(transferToBank.getDebitAmount());
        return "redirect:/profile";
    }

    @PostMapping("/profile/credit")
    public String CreditFromBank(@Validated @ModelAttribute("creditAmountDTO") CreditToBankDto creditAmountDto, BindingResult result,Model model){
        if(result.hasErrors()){
            //display validation errors
            List<String> errorList = new ArrayList<String>();
            for(ObjectError error : result.getAllErrors()){
                errorList.add(error.getDefaultMessage());
            }
            model.addAttribute("ValidationError", errorList);
            return "/profile";
        }

        if(creditAmountDto == null | creditAmountDto.getCreditAmount().equals(BigDecimal.valueOf(0))){
            throw new BalanceException("Amount cannot be empty");
        }
        userAccountService.creditBalance(creditAmountDto.getCreditAmount());
        return "redirect:/profile";
    }

}
