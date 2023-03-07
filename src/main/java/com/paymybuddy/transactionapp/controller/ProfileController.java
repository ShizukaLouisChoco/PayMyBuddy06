package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.dto.CreditToBankDto;
import com.paymybuddy.transactionapp.dto.DebitToBankDto;
import com.paymybuddy.transactionapp.service.ConnectedUserDetailsService;
import com.paymybuddy.transactionapp.service.UserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class ProfileController {


    private final ConnectedUserDetailsService connectedUserDetailsService;

    private final UserAccountService userAccountService;


    @RequestMapping(value="/profile", method = RequestMethod.GET)
    public String profile(Model model){
        model.addAttribute("userAccount", connectedUserDetailsService.getConnectedUser());
        model.addAttribute("transferDto", new DebitToBankDto());
        model.addAttribute("creditAmount",BigDecimal.ZERO);
        model.addAttribute("creditAmountDto", new CreditToBankDto());
        return "/profile";
    }

    @PostMapping("/profile/debit")
    public String DebitToBank(@Valid @ModelAttribute("transferDto") DebitToBankDto transferToBank, BindingResult result, Model model){
        model.addAttribute("userAccount", connectedUserDetailsService.getConnectedUser());
        model.addAttribute("transferDto", transferToBank);
        model.addAttribute("creditAmount",BigDecimal.ZERO);
        model.addAttribute("creditAmountDto", new CreditToBankDto());
        //validation errors
        if(result.hasErrors()){
            return "profile";
        }
        //exception handling
        try{
           userAccountService.debitBalance(transferToBank.getDebitAmount());
        }catch(Exception ex){
            model.addAttribute("userAccount", connectedUserDetailsService.getConnectedUser());
            model.addAttribute("transferDto", transferToBank);
            model.addAttribute("creditAmount",BigDecimal.ZERO);
            model.addAttribute("creditAmountDto", new CreditToBankDto());
            model.addAttribute("errorMsg", ex.getMessage());
            return "profile";
        }

        return "redirect:/profile";
    }

    @PostMapping("/profile/credit")
    public String CreditFromBank(@Valid @ModelAttribute("creditAmountDto") CreditToBankDto creditAmountDto, BindingResult result, Model model){
        model.addAttribute("userAccount", connectedUserDetailsService.getConnectedUser());
        model.addAttribute("transferDto", new DebitToBankDto());
        model.addAttribute("creditAmount",BigDecimal.ZERO);
        model.addAttribute("creditAmountDto", creditAmountDto);
        //validation errors
        if(result.hasErrors()){
            return "profile";
        }
        //exception handling
        try{
            userAccountService.creditBalance(creditAmountDto.getCreditAmount());
        }catch(Exception ex){
            model.addAttribute("userAccount", connectedUserDetailsService.getConnectedUser());
            model.addAttribute("transferDto", new DebitToBankDto());
            model.addAttribute("creditAmount",BigDecimal.ZERO);
            model.addAttribute("creditAmountDto", creditAmountDto);
            model.addAttribute("errorMsg", ex.getMessage());
            return "profile";
        }
        return "redirect:/profile";
    }

}
