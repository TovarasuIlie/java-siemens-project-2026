package com.example.backend.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {
    @NotNull
    private Integer trainId;
    @NotNull
    private Integer startStationId;
    @NotNull
    private Integer endStationId;
    @NotNull
    @Min(1)
    private Integer seatsBooked;
    @NotNull
    @Future
    private LocalDate travelDate;
}
