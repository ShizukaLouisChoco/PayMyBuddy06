package com.paymybuddy.transactionapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @PrimaryKeyJoinColumn
    @JoinColumn(nullable = false)
    private UserAccount creditor;

    @ManyToOne
    @PrimaryKeyJoinColumn
    @JoinColumn(nullable = false)
    private UserAccount debtor;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal fee;

    public BigDecimal getAmountForDebtor(){
        fee = BigDecimal.valueOf(0.05);
        return amount.subtract(amount.multiply(fee));
    }

}
