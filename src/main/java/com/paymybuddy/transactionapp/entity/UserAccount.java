package com.paymybuddy.transactionapp.entity;

import com.paymybuddy.transactionapp.exception.BalanceException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Entity
@Table(name = "user_account")
public class UserAccount implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 2, max = 30)
    private String email;

    @Column(nullable = false)
    private String username;


    @Column(nullable = false)
    @Size(min = 5)
    private String password;

    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToMany(cascade =  CascadeType.PERSIST, fetch=FetchType.EAGER)
    private List<UserAccount> friends = new ArrayList<>();

    public UserAccount(Long id) {
        this.id = id;
    }


    public boolean friendExists(Long id){
        return friends.stream().anyMatch(f -> f.id.equals(id));
    }


    public void debitAmount(BigDecimal amount) {
        if(balance.compareTo(amount) < 0) {
            throw new BalanceException("Your balance is not enough to transfer this amount");
        }
        balance=balance.subtract(amount);
    }

    public void creditAmount(BigDecimal amount) {
        balance=balance.add(amount);
    }
}
