package com.paymybuddy.transactionapp.entity;

import com.paymybuddy.transactionapp.dto.TransactionDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

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

    @Column(nullable = false)
    private String description;

    @Column
    private BigDecimal creditAmount;

    public Transaction(TransactionDto transaction, Long debtorId) {
        creditor = new UserAccount(transaction.getCreditorId());
        debtor = new UserAccount(debtorId);
        amount = transaction.getAmount();
        description = transaction.getDescription();

    }

    @PrePersist
    public void setCreditAmount() {
        //fee is 0.5%
        final BigDecimal FEE = BigDecimal.valueOf(0.995);
        if (this.amount != null) {
            BigDecimal calculatedAmount = amount.multiply(FEE);
            BigDecimal scaledAmount = calculatedAmount.setScale(2, RoundingMode.HALF_UP);
            this.creditAmount = scaledAmount;
        }
    }
}
