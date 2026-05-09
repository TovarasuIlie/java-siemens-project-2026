package com.example.backend.dto;

public record LoginResult (
        String accessToken,
        String refreshToken
) {
}
