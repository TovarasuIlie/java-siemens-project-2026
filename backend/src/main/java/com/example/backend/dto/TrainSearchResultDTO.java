package com.example.backend.dto;

import com.example.backend.enums.TrainCategory;

import java.time.LocalTime;

public record TrainSearchResultDTO(
        String trainNumber,
        TrainCategory trainType,
        LocalTime departureTime,
        LocalTime arrivalTime,
        Integer delayMinutes
) {
}
