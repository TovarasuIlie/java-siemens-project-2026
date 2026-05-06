package com.example.backend.DTOs;

import com.example.backend.Enums.TrainCategory;

import java.time.LocalTime;

public record TrainSearchResultDTO(
        String trainNumber,
        TrainCategory trainType,
        LocalTime departureTime,
        LocalTime arrivalTime,
        Integer delayMinutes
) {
}
