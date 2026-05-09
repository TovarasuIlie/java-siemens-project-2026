package com.example.backend.service;

import com.example.backend.dto.Response;
import com.example.backend.dto.booking.BookingRequest;
import com.example.backend.dto.booking.BookingResponse;
import com.example.backend.entity.*;
import com.example.backend.exeption.BadRequestException;
import com.example.backend.exeption.NotFoundException;
import com.example.backend.repository.BookingRepository;
import com.example.backend.repository.StationRepository;
import com.example.backend.repository.TrainRepository;
import com.example.backend.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public List<Response> createBooking(List<BookingRequest> requests, String customerEmail) throws NotFoundException, BadRequestException, MessagingException {
        List<Response> responses = new ArrayList<>();

        for (BookingRequest req : requests) {
            responses.add(saveBooking(req, customerEmail));
        }

        return responses;
    }

    private Response saveBooking(BookingRequest request, String customerEmail) throws MessagingException, NotFoundException, BadRequestException {
        Train train = trainRepository.findById(request.getTrainId())
                .orElseThrow(() -> new NotFoundException(
                        "Train not found with id: " + request.getTrainId()));

        Station startStation = stationRepository.findById(request.getStartStationId())
                .orElseThrow(() -> new NotFoundException(
                        "Station not found with id: " + request.getStartStationId()));

        Station endStation = stationRepository.findById(request.getEndStationId())
                .orElseThrow(() -> new NotFoundException(
                        "Station not found with id: " + request.getEndStationId()));

        validateStationsOnRoute(train, startStation, endStation);

        int alreadyBooked = bookingRepository
                .sumBookedSeatsForTrainOnDate(train.getId(), request.getTravelDate());

        int availableSeats = train.getTotalCapacity() - alreadyBooked;

        if (request.getSeatsBooked() > availableSeats) {
            throw new BadRequestException(
                    "Not enough seats available. Requested: " + request.getSeatsBooked() +
                            ", Available: " + availableSeats
            );
        }

        User user = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Booking booking = Booking.builder()
                .user(user)
                .train(train)
                .seatsBooked(request.getSeatsBooked())
                .startStation(startStation)
                .endStation(endStation)
                .travelDate(request.getTravelDate())
                .bookingDate(OffsetDateTime.now())
                .build();

        booking = bookingRepository.save(booking);

        emailService.sendBookingConfirmationEmail(booking);

        return new Response(HttpStatus.CREATED, "Booking confirmed! A confirmation email has been sent.");
    }


    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(String customerEmail) {
        return bookingRepository.findByUser_Email(customerEmail).stream()
                .map(b ->
                        BookingResponse.builder()
                        .bookingId(b.getId())
                        .trainNumber(b.getTrain().getTrainNumber())
                        .startStation(b.getStartStation().getName())
                        .endStation(b.getEndStation().getName())
                        .seatsBooked(b.getSeatsBooked())
                        .travelDate(b.getTravelDate())
                        .bookingDate(b.getBookingDate())
                        .build()
                )
                .toList();
    }

    private void validateStationsOnRoute(Train train, Station start, Station end) throws BadRequestException {
        List<RouteStation> routeStations = train.getRoute().getStations();

        List<Integer> stationIds = routeStations.stream()
                .map(rs -> rs.getStation().getId())
                .toList();

        if (!stationIds.contains(start.getId())) {
            throw new BadRequestException(
                    "Start station '" + start.getName() + "' is not on this train's route.");
        }
        if (!stationIds.contains(end.getId())) {
            throw new BadRequestException(
                    "End station '" + end.getName() + "' is not on this train's route.");
        }

        int startOrder = routeStations.stream()
                .filter(rs -> rs.getStation().getId().equals(start.getId()))
                .findFirst().get().getStopOrder();

        int endOrder = routeStations.stream()
                .filter(rs -> rs.getStation().getId().equals(end.getId()))
                .findFirst().get().getStopOrder();

        if (startOrder >= endOrder) {
            throw new BadRequestException(
                    "Start station must come before end station on the route.");
        }
    }
}
