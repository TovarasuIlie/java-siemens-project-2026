package com.example.backend.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDTO {
    @NotBlank(message = "Email-ul nu poate fi gol")
    @Email(message = "Format email invalid")
    private String email;

    @NotBlank(message = "Parola nu poate fi goală")
    private String password;
}
