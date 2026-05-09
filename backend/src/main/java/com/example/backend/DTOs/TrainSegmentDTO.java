package com.example.backend.DTOs;

import java.time.LocalTime;

public record TrainSegmentDTO(
        String trainNumber,
        String fromStation,
        String toStation,
        LocalTime departureTime,
        LocalTime arrivalTime,
        int fromStopOrder,
        int toStopOrder
) {
}
