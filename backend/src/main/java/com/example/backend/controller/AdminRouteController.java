package com.example.backend.controller;

import com.example.backend.dto.AdminDTO;
import com.example.backend.exeption.NotFoundException;
import com.example.backend.service.admin.AdminRouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/routes")
@RequiredArgsConstructor
public class AdminRouteController {

    private final AdminRouteService adminRouteService;

    @PostMapping
    public ResponseEntity<AdminDTO.RouteResponse> addRoute(@Valid @RequestBody AdminDTO.RouteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminRouteService.addRoute(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminDTO.RouteResponse> updateRoute(
            @PathVariable Integer id,
            @Valid @RequestBody AdminDTO.RouteRequest request) throws NotFoundException {
        return ResponseEntity.ok(adminRouteService.updateRoute(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Integer id) throws NotFoundException {
        adminRouteService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }
}
