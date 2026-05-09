package com.example.backend.dto;

import java.util.List;

public record JourneyDTO(
        List<TrainSegmentDTO> segments
) {
}
