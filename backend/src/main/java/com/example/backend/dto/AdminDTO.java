package com.example.backend.dto;

import com.example.backend.enums.TrainCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AdminDTO {
    @Data
    public static class TrainRequest {
        @NotBlank
        private String trainNumber;
        @NotNull
        private TrainCategory trainType;
        @NotNull
        private Integer routeId;
        @NotNull
        @Min(1)
        private Integer totalCapacity;
    }

    @Data
    @Builder
    public static class TrainResponse {
        private Integer id;
        private String trainNumber;
        private TrainCategory trainType;
        private String routeName;
        private Integer totalCapacity;
        private Integer delayMinutes;
    }

    @Data
    public static class RouteRequest {
        @NotBlank
        private String routeName;
        @NotEmpty
        private List<RouteStationRequest> stations;
    }

    @Data
    public static class RouteStationRequest {
        @NotNull
        private Integer stationId;
        @NotNull
        private Integer stopOrder;
        private LocalTime arrivalTime;
        private LocalTime departureTime;
    }

    @Data
    @Builder
    public static class RouteResponse {
        private Integer id;
        private String routeName;
        private List<String> stations;
    }

    @Data
    @Builder
    public static class BookingResponse {
        private Integer bookingId;
        private String customerEmail;
        private String trainNumber;
        private String startStation;
        private String endStation;
        private Integer seatsBooked;
        private LocalDate travelDate;
    }
}
