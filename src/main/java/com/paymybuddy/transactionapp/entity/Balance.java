package com.paymybuddy.transactionapp.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Balance implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserAccount user;

    private BigDecimal userBalance;

    private BigDecimal credit;

    private BigDecimal debit;
    @OneToMany
    private List<Transaction> history;


}
