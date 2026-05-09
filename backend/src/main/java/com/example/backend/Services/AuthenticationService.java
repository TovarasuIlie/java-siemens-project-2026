package com.example.backend.Services;


import com.example.backend.DTOs.*;
import com.example.backend.Entities.User;
import com.example.backend.Enums.UserCategory;
import com.example.backend.Exeptions.BadRequestException;
import com.example.backend.Repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public ResponseEntity<Response> register(UserRegisterDTO newUser) throws BadRequestException {
        if(userRepository.existsByEmail(newUser.getEmail())) {
            throw new BadRequestException("Acest email este deja folosit!");
        }

        User user = User.builder()
                .email(newUser.getEmail())
                .password(passwordEncoder.encode(newUser.getPassword()))
                .role(UserCategory.CUSTOMER)
                .build();

        userRepository.saveAndFlush(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(HttpStatus.CREATED, "Contul a fost creat cu success!"));
    }

    public LoginResult login(UserLoginDTO login) throws BadRequestException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword())
            );

            User user = (User) authentication.getPrincipal();

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            return new LoginResult(accessToken, refreshToken);

        } catch (AuthenticationException e) {
            throw new BadRequestException("Email sau parolă incorectă");
        }
    }

    public ResponseEntity<?> refreshToken(String refreshToken) throws BadRequestException {
        String email = jwtService.extractEmail(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Adresa de email nu a fost gasita!"));

        String newAccessToken = jwtService.generateAccessToken(user);
        return ResponseEntity.ok(new AuthResponse(newAccessToken));
    }
}
