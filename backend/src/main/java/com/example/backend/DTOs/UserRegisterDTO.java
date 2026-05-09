package com.example.backend.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterDTO {

    @NotBlank(message = "Email-ul este obligatoriu")
    @Email(message = "Formatul email-ului este invalid")
    private String email;

    @NotBlank(message = "Parola este obligatorie")
    @Size(min = 8, max = 20, message = "Parola trebuie să aibă între 8 și 20 de caractere")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Parola trebuie să conțină cel puțin o cifră, o literă mică, o literă mare și un caracter special (@#$%^&+=!)"
    )
    private String password;
}
