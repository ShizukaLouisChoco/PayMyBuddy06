package com.paymybuddy.transactionapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.GenerationType.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private UUID id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String password;
    @OneToOne(mappedBy = "user")
    private Balance balance;
    @OneToMany
    private List<User> friends;

    public User update(User user){
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.password = user.getPassword();
        return user;
    }
}
