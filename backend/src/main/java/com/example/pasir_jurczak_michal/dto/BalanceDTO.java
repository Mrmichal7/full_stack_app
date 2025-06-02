package com.example.pasir_jurczak_michal.dto;

import lombok.Data;

@Data
public class BalanceDTO {
    private double totalIncome;
    private double totalExpense;
    private double balance;

    public BalanceDTO(double income, double expense, double balance) {
        this.totalIncome = income;
        this.totalExpense = expense;
        this.balance = balance;
    }
}