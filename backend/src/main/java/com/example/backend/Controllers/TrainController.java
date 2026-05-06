package com.example.backend.Controllers;

import com.example.backend.DTOs.JourneyDTO;
import com.example.backend.DTOs.TrainSearchResultDTO;
import com.example.backend.Exeptions.NotFoundException;
import com.example.backend.Services.TrainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
@CrossOrigin(origins = "*")
public class TrainController {
    private final TrainService trainService;

    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrainSearchResultDTO>> getTrainsBetween(@RequestParam String from, @RequestParam String to) throws NotFoundException {
        return ResponseEntity.ok(trainService.searchTrains(from, to));
    }

    @GetMapping("/search-with-changes")
    public ResponseEntity<List<JourneyDTO>> getTrainsBetweenWithChanges(@RequestParam String from, @RequestParam String to) throws NotFoundException {
        return ResponseEntity.ok(trainService.findComplexRoute(from, to, 1));
    }
}
