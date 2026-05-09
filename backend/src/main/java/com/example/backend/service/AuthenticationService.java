package com.example.backend.service;


import com.example.backend.dto.*;
import com.example.backend.entity.User;
import com.example.backend.enums.UserCategory;
import com.example.backend.exeption.BadRequestException;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public void register(UserRegisterDTO newUser) throws BadRequestException {
        if(userRepository.existsByEmail(newUser.getEmail())) {
            throw new BadRequestException("Acest email este deja folosit!");
        }

        User user = User.builder()
                .email(newUser.getEmail())
                .password(passwordEncoder.encode(newUser.getPassword()))
                .role(UserCategory.CUSTOMER)
                .build();

        userRepository.save(user);
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

    public AuthResponse refreshToken(String refreshToken) throws BadRequestException {
        String email = jwtService.extractEmail(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Adresa de email nu a fost gasita!"));

        String newAccessToken = jwtService.generateAccessToken(user);
        return new AuthResponse(newAccessToken);
    }
}
