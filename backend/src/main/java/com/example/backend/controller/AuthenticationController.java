package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.exeption.BadRequestException;
import com.example.backend.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RequestMapping("api/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("register")
    public ResponseEntity<Response> register(@Valid @RequestBody UserRegisterDTO request) throws BadRequestException, MessagingException, IOException {
            return authenticationService.register(request);
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody UserLoginDTO request, HttpServletResponse response) throws BadRequestException {

        LoginResult loginResult = authenticationService.login(request);

        Cookie refreshTokenCookie = new Cookie("refreshToken", loginResult.refreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(new AuthResponse(loginResult.accessToken()));
    }

    @PostMapping("refresh")
    public ResponseEntity<?> refreshToken(@CookieValue("refreshToken") String refreshToken) throws BadRequestException {
        return authenticationService.refreshToken(refreshToken);
    }
}
