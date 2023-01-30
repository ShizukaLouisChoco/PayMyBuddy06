package com.paymybuddy.transactionapp.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Balance implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Long credit = 0L;
    private Long debt = 0L ;

    @OneToMany
    private List<Transaction> history;


    public Long getSum(){
        return credit - debt;
    }
}
