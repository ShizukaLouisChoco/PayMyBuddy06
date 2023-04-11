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

    /**
     * profile page has debit and credit button
     * url : "<a href="http://localhost:8080/profile">...</a>"
     */
    @RequestMapping(value="/profile", method = RequestMethod.GET)
    public String profile(Model model){
        model.addAttribute("userAccount", connectedUserDetailsService.getConnectedUser());
        model.addAttribute("transferDto", new DebitToBankDto());
        model.addAttribute("creditAmount",BigDecimal.ZERO);
        model.addAttribute("creditAmountDto", new CreditToBankDto());
        return "/profile";
    }

    /**
     * debit button can transfer balance to bank account
     * url : "<a href="http://localhost:8080/profile/debit">...</a>"
     */
    @PostMapping("/profile/debit")
    public String DebitToBank(@Valid @ModelAttribute("transferDto") DebitToBankDto transferToBank, BindingResult result, Model model){
        model.addAttribute("userAccount", connectedUserDetailsService.getConnectedUser());
        model.addAttribute("transferDto", transferToBank);
        model.addAttribute("creditAmount",BigDecimal.ZERO);
        model.addAttribute("creditAmountDto", new CreditToBankDto());
        //validation errors
        if(result.hasErrors()){
            return "/profile";
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
            return "/profile";
        }

        return "redirect:/profile";
    }
    /**
     * credit button can add money to balance
     * url : "<a href="http://localhost:8080/profile/credit">...</a>"
     */

    @PostMapping("/profile/credit")
    public String CreditFromBank(@Valid @ModelAttribute("creditAmountDto") CreditToBankDto creditAmountDto, BindingResult result, Model model){
        model.addAttribute("userAccount", connectedUserDetailsService.getConnectedUser());
        model.addAttribute("transferDto", new DebitToBankDto());
        model.addAttribute("creditAmount",BigDecimal.ZERO);
        model.addAttribute("creditAmountDto", creditAmountDto);
        //validation errors
        if(result.hasErrors()){
            return "/profile";
        }
        userAccountService.creditBalance(creditAmountDto.getCreditAmount());
        return "redirect:/profile";
    }

}
