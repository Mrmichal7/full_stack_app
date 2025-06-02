package com.example.pasir_jurczak_michal.dto;

import com.example.pasir_jurczak_michal.model.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransactionDTO {

    @NotNull(message = "Kwota nie może być pusta")
    @Min(value = 1, message = "Kwota musi być większa od 0")
    private Double amount;

    @NotNull(message = "Typ transakcji jest wymagany")
    @Pattern(regexp = "INCOME|EXPENSE", message = "Typ transakcji musi byc INCOME lub EXPENSE")
    private String type;

    @Size(max = 50, message = "Tagi nie mogą przekraczać 50 znaków")
    private String tags;

    @Size(max = 255, message = "Notatka może mieć maksymalnie 255 znaków")
    private String notes;
}