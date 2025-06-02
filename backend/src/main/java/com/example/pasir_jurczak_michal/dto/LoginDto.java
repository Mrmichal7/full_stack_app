package com.example.pasir_jurczak_michal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Podaj poprawny adres email")
    private String email;

    @NotBlank(message = "Hasło nie może być puste")
    private String password;
}