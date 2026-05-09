package com.example.backend.controller;

import com.example.backend.dto.AdminDTO;
import com.example.backend.dto.Response;
import com.example.backend.exeption.NotFoundException;
import com.example.backend.service.admin.AdminTrainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/trains")
@RequiredArgsConstructor
public class AdminTrainController {

    private final AdminTrainService adminTrainService;

    @PostMapping
    public ResponseEntity<AdminDTO.TrainResponse> addTrain(@Valid @RequestBody AdminDTO.TrainRequest request) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminTrainService.addTrain(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminDTO.TrainResponse> updateTrain(
            @PathVariable Integer id,
            @Valid @RequestBody AdminDTO.TrainRequest request) throws NotFoundException {
        return ResponseEntity.ok(adminTrainService.updateTrain(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Integer id) throws NotFoundException {
        adminTrainService.deleteTrain(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<AdminDTO.BookingResponse>> getBookingsForTrain(@PathVariable Integer id) throws NotFoundException {
        return ResponseEntity.ok(adminTrainService.getBookingsForTrain(id));
    }

    @PostMapping("/{id}/delay")
    public ResponseEntity<Response> reportDelay(
            @PathVariable Integer id,
            @RequestParam int delayMinutes) throws NotFoundException {
        adminTrainService.reportDelay(id, delayMinutes);
        return ResponseEntity.ok(new Response(HttpStatus.OK,
                "Delay reported and customers notified."));
    }
}
