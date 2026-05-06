package com.example.backend.DTOs;

import java.time.LocalTime;

public record Edge(
        String trainNumber,
        String toStation,
        LocalTime departure,
        LocalTime arrival,
        int routeId
) {
}
