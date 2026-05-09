package com.example.backend.dto;

import org.springframework.http.HttpStatus;

public record Response(
        HttpStatus status,
        Object message
) { }
