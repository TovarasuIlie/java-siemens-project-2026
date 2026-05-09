package com.example.backend.dto.booking;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Builder
public class BookingResponse {
    private Integer bookingId;
    private String trainNumber;
    private String startStation;
    private String endStation;
    private Integer seatsBooked;
    private LocalDate travelDate;
    private OffsetDateTime bookingDate;
}
