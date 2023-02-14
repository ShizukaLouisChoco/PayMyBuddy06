package com.paymybuddy.transactionapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Email address is required")
    @Column(nullable = false, unique=true)
    @Size(min = 5, max = 30)
    private String email;

    @NotBlank(message = "username is required")
    @Column(nullable = false)
    private String username;


    @Column(nullable = false)
    @NotBlank(message = "password is required")
    @Size(min = 5)
    private String password;

    private BigDecimal balance = BigDecimal.ZERO;

    @OneToMany
    private List<UserAccount> connections = new ArrayList<>();


    public boolean friendExists(Long id){
        return connections.stream().anyMatch(f -> f.id.equals(id));
    }


    public void debitAmount(BigDecimal amount) {
        balance.subtract(amount);
    }

    public void creditAmount(BigDecimal amount) {
        balance.add(amount);
    }
}
