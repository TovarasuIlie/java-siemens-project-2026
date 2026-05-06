package com.example.backend.DTOs;

import java.util.List;

public record JourneyDTO(
        List<TrainSegmentDTO> segments
) {
}
