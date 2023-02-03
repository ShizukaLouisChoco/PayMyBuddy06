package com.paymybuddy.transactionapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class UserAccount implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false, unique=true)
    private String email;

    @Column(nullable = false)
    private String username;


    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "user")
    private Balance balance;

    @OneToMany
    private List<UserAccount> connections = new ArrayList<>();

    public UserAccount update(UserAccount user){
        this.username = user.getUsername();
        this.password = user.getPassword();
        return user;
    }

    public void addConnections(UserAccount userAccount){
        connections.add(userAccount);
        userAccount.connections.add(this);
    }
}
