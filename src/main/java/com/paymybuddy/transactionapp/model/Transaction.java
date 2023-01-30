package com.paymybuddy.transactionapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private UUID id;
    @OneToOne
    @Column(nullable = false)
    private User creditor;
    @OneToOne
    @Column(nullable = false)
    private User debtor;
    @Column(nullable = false)
    private LocalDateTime date;
    @Column(nullable = false)
    private Long amount;
    @Column(nullable = false)
    private Long fee;

    public Long getFinalAmount(){
        return amount-fee;
    }

}
