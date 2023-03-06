package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.dto.CreditToBankDto;
import com.paymybuddy.transactionapp.dto.DebitToBankDto;
import com.paymybuddy.transactionapp.service.UserAccountService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;

@Controller
public class ProfileController {


    private final UserAccountService userAccountService;

    public ProfileController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @RequestMapping(value="/profile", method = RequestMethod.GET)
    public String profile(Model model){
        model.addAttribute("userAccount", userAccountService.getConnectedUser());
        model.addAttribute("transferDto", new DebitToBankDto());
        model.addAttribute("creditAmount",BigDecimal.ZERO);
        model.addAttribute("creditAmountDto", new CreditToBankDto());
        return "/profile";
    }

    @PostMapping("/profile/debit")
    public String DebitToBank(@Valid @ModelAttribute("transferDto") DebitToBankDto transferToBank, BindingResult result, Model model){
        model.addAttribute("userAccount", userAccountService.getConnectedUser());
        model.addAttribute("transferDto", transferToBank);
        model.addAttribute("creditAmount",BigDecimal.ZERO);
        model.addAttribute("creditAmountDto", new CreditToBankDto());
        if(result.hasErrors()){
            return "profile";
            //display validation errors
            /*List<String> errorList = new ArrayList<String>();
            for(ObjectError error : result.getAllErrors()){
                errorList.add(error.getDefaultMessage());
            }
            model.addAttribute("errorMsg", errorList);
            */
        }
        try{
           userAccountService.debitBalance(transferToBank.getDebitAmount());
        }catch(Exception ex){
            model.addAttribute("userAccount", userAccountService.getConnectedUser());
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
            //validation errors
        model.addAttribute("userAccount", userAccountService.getConnectedUser());
        model.addAttribute("transferDto", new DebitToBankDto());
        model.addAttribute("creditAmount",BigDecimal.ZERO);
        model.addAttribute("creditAmountDto", creditAmountDto);
        if(result.hasErrors()){
            return "profile";
            /*
            List<String> errorList = new ArrayList<>();
            for(ObjectError error : result.getAllErrors()){
                errorList.add(error.getDefaultMessage());
            }
            model.addAttribute("errorMsg", errorList);*/
        }
        try{
        userAccountService.creditBalance(creditAmountDto.getCreditAmount());
        }catch(Exception ex){
            model.addAttribute("userAccount", userAccountService.getConnectedUser());
            model.addAttribute("transferDto", new DebitToBankDto());
            model.addAttribute("creditAmount",BigDecimal.ZERO);
            model.addAttribute("creditAmountDto", creditAmountDto);
            model.addAttribute("errorMsg", ex.getMessage());
            return "profile";
        }
        return "redirect:/profile";
    }

}
