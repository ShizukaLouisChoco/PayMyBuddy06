package com.paymybuddy.transactionapp.entity;

import com.paymybuddy.transactionapp.dto.TransactionDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="transaction")
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
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

 //   private float fee;

    @Column(nullable = false)
    private String description;

    @Column
    private BigDecimal creditAmount;

    public Transaction(TransactionDto transaction) {
        creditor = new UserAccount(transaction.getCreditorId());
        debtor = new UserAccount(transaction.getCreditorId());
        amount = transaction.getAmount();
        description = transaction.getDescription();

    }

    @PrePersist
    public void setDebitAmount() {
        if (this.amount != null) {
            this.creditAmount = amount.multiply(BigDecimal.valueOf(0.95));
        }
    }
}
