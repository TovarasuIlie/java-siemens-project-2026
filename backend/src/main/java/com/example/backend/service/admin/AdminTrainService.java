package com.example.backend.service.admin;

import com.example.backend.dto.AdminDTO;
import com.example.backend.entity.Booking;
import com.example.backend.entity.Route;
import com.example.backend.entity.Train;
import com.example.backend.exeption.NotFoundException;
import com.example.backend.repository.BookingRepository;
import com.example.backend.repository.RouteRepository;
import com.example.backend.repository.TrainRepository;
import com.example.backend.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminTrainService {

    private final TrainRepository trainRepository;
    private final RouteRepository routeRepository;
    private final BookingRepository bookingRepository;
    private final EmailService emailService;

    public AdminDTO.TrainResponse addTrain(AdminDTO.TrainRequest request) throws NotFoundException {
        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new NotFoundException(
                        "Route not found with id: " + request.getRouteId()));

        Train train = Train.builder()
                .trainNumber(request.getTrainNumber())
                .trainType(request.getTrainType())
                .route(route)
                .totalCapacity(request.getTotalCapacity())
                .delayMinutes(0)
                .build();

        train = trainRepository.saveAndFlush(train);

        return AdminDTO.TrainResponse.builder()
                .id(train.getId())
                .trainNumber(train.getTrainNumber())
                .trainType(train.getTrainType())
                .routeName(train.getRoute().getRouteName())
                .totalCapacity(train.getTotalCapacity())
                .delayMinutes(train.getDelayMinutes())
                .build();
    }

    public AdminDTO.TrainResponse updateTrain(Integer id, AdminDTO.TrainRequest request) throws NotFoundException {
        Train train = trainRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Train not found with id: " + id));

        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new NotFoundException(
                        "Route not found with id: " + request.getRouteId()));

        train.setTrainNumber(request.getTrainNumber());
        train.setTrainType(request.getTrainType());
        train.setRoute(route);
        train.setTotalCapacity(request.getTotalCapacity());

        train = trainRepository.saveAndFlush(train);

        return AdminDTO.TrainResponse.builder()
                .id(train.getId())
                .trainNumber(train.getTrainNumber())
                .trainType(train.getTrainType())
                .routeName(train.getRoute().getRouteName())
                .totalCapacity(train.getTotalCapacity())
                .delayMinutes(train.getDelayMinutes())
                .build();
    }

    public void deleteTrain(Integer id) throws NotFoundException {
        Train train = trainRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Train not found with id: " + id));
        trainRepository.delete(train);
    }

    @Transactional(readOnly = true)
    public List<AdminDTO.BookingResponse> getBookingsForTrain(Integer trainId) throws NotFoundException {
        if (!trainRepository.existsById(trainId)) {
            throw new NotFoundException(
                    "Train not found with id: " + trainId);
        }

        return bookingRepository.findByTrain_Id(trainId).stream()
                .map(b ->
                        AdminDTO.BookingResponse.builder()
                                .bookingId(b.getId())
                                .customerEmail(b.getUser().getEmail())
                                .trainNumber(b.getTrain().getTrainNumber())
                                .startStation(b.getStartStation().getName())
                                .endStation(b.getEndStation().getName())
                                .seatsBooked(b.getSeatsBooked())
                                .travelDate(b.getTravelDate())
                                .build()
                )
                .toList();
    }

    public void reportDelay(Integer trainId, int delayMinutes) throws NotFoundException {
        Train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new NotFoundException(
                        "Train not found with id: " + trainId));

        train.setDelayMinutes(delayMinutes);
        trainRepository.save(train);

        List<Booking> affected = bookingRepository
                .findUpcomingBookingsByTrainId(trainId, LocalDate.now());

        affected.forEach(booking ->
        {
            try {
                emailService.sendDelayNotificationEmail(booking, delayMinutes);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
