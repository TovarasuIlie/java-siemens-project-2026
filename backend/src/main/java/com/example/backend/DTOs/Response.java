package com.example.backend.DTOs;

import org.springframework.http.HttpStatus;

public record Response(
        HttpStatus status,
        String message
) { }
