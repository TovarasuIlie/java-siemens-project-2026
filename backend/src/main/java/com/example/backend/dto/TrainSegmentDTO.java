package com.example.backend.dto;

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
