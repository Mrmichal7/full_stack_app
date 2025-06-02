package com.example.pasir_jurczak_michal.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupTransactionDTO {
    private Long groupId; // ID groupY
    private Double amount; // Kwota (+ = Zarobek, - = Wydatek)
    private String type; // "INCOME" lub "EXPENSE"
    private String title;
    private List<Long> selectedUserIds;
}