package de.devcyntrix.sqleco.model;

import java.util.UUID;

public class PlayerAccount {

    private UUID id;

    private double balance;

    public PlayerAccount(UUID id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
