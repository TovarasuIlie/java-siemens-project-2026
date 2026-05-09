package com.example.backend.service.admin;

import com.example.backend.dto.AdminDTO;
import com.example.backend.entity.Route;
import com.example.backend.entity.RouteStation;
import com.example.backend.entity.Station;
import com.example.backend.exeption.NotFoundException;
import com.example.backend.repository.RouteRepository;
import com.example.backend.repository.RouteStationRepository;
import com.example.backend.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminRouteService {

    private final RouteRepository routeRepository;
    private final StationRepository stationRepository;
    private final RouteStationRepository routeStationRepository;

    public AdminDTO.RouteResponse addRoute(AdminDTO.RouteRequest request) {
        Route route = Route.builder()
                .routeName(request.getRouteName())
                .stations(new ArrayList<>())
                .build();

        route = routeRepository.save(route);

        // Adauga statiile in ordine
        Route finalRoute = route;
        List<RouteStation> routeStations = request.getStations().stream()
                .map(s -> {
                    try {
                        return buildRouteStation(s, finalRoute);
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        routeStationRepository.saveAll(routeStations);
        route.setStations(routeStations);

        return toRouteResponse(route);
    }

    public AdminDTO.RouteResponse updateRoute(Integer id, AdminDTO.RouteRequest request) throws NotFoundException {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Route not found with id: " + id));

        route.setRouteName(request.getRouteName());

        // Sterge statiile vechi si adauga cele noi
        routeStationRepository.deleteAll(route.getStations());

        List<RouteStation> newStations = request.getStations().stream()
                .map(s -> {
                    try {
                        return buildRouteStation(s, route);
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        routeStationRepository.saveAll(newStations);
        route.setStations(newStations);

        return toRouteResponse(routeRepository.save(route));
    }

    public void deleteRoute(Integer id) throws NotFoundException {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Route not found with id: " + id));
        routeRepository.delete(route);
    }

    private RouteStation buildRouteStation(AdminDTO.RouteStationRequest req, Route route) throws NotFoundException {
        Station station = stationRepository.findById(req.getStationId())
                .orElseThrow(() -> new NotFoundException(
                        "Station not found with id: " + req.getStationId()));

        return RouteStation.builder()
                .route(route)
                .station(station)
                .stopOrder(req.getStopOrder())
                .arrivalTime(req.getArrivalTime())
                .departureTime(req.getDepartureTime())
                .build();
    }

    private AdminDTO.RouteResponse toRouteResponse(Route route) {
        List<String> stationNames = route.getStations().stream()
                .map(rs -> rs.getStation().getName())
                .toList();

        return AdminDTO.RouteResponse.builder()
                .id(route.getId())
                .routeName(route.getRouteName())
                .stations(stationNames)
                .build();
    }
}
