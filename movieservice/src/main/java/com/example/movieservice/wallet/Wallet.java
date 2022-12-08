package com.example.movieservice.wallet;

import com.example.movieservice.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Long id;

    private int moneyAmmount;

    @OneToOne(mappedBy = "wallet")
    @JsonIgnore
    private User user;

    public Wallet() {
        this.moneyAmmount = 0;
    }

    //GETTERS
    public Long getId() {
        return id;
    }

    public int getMoneyAmmount() {
        return moneyAmmount;
    }

    public User getUser() {
        return user;
    }

    //SETTERS
    public void setMoneyAmmount(int moneyAmmount) {
        this.moneyAmmount = moneyAmmount;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "moneyAmmount=" + moneyAmmount +
                '}';
    }
}
