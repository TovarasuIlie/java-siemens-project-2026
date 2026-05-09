package com.example.backend.DTOs;

import java.time.LocalTime;
import java.util.List;

public record ScoredPath(
        List<TrainSegmentDTO> path,
        LocalTime firstDeparture,
        long gCost
) implements Comparable<ScoredPath> {
    @Override
    public int compareTo(ScoredPath other) {
        return Long.compare(this.gCost, other.gCost);
    }
}
