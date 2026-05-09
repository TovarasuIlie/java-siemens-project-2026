package com.example.backend.DTOs;

public record LoginResult (
        String accessToken,
        String refreshToken
) {
}
