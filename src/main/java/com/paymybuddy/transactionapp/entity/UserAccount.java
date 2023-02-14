package com.paymybuddy.transactionapp.entity;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.paymybuddy.transactionapp.security.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_account")
public class UserAccount implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique=true)
    private String email;

    @Column(nullable = false)
    private String username;


    @Column(nullable = false)
    private String password;

    private BigDecimal balance = BigDecimal.ZERO;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new Role("ROLE_USER"));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
