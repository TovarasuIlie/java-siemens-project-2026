package com.example.backend.controller;

import com.example.backend.dto.Response;
import com.example.backend.dto.booking.BookingRequest;
import com.example.backend.dto.booking.BookingResponse;
import com.example.backend.exeption.BadRequestException;
import com.example.backend.exeption.NotFoundException;
import com.example.backend.service.BookingService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/booking")
@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<List<Response>> createBooking(@Valid @RequestBody List<BookingRequest> request, @AuthenticationPrincipal UserDetails currentUser) throws NotFoundException, BadRequestException, MessagingException {
        String email = currentUser.getUsername();
        List<Response> response = bookingService.createBooking(request, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookingResponse>> getMyBookings(
            @AuthenticationPrincipal UserDetails currentUser) {

        return ResponseEntity.ok(
                bookingService.getMyBookings(currentUser.getUsername()));
    }
}
