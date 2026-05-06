package com.example.backend.Services;

import com.example.backend.DTOs.Edge;
import com.example.backend.DTOs.JourneyDTO;
import com.example.backend.DTOs.TrainSearchResultDTO;
import com.example.backend.DTOs.TrainSegmentDTO;
import com.example.backend.Exeptions.NotFoundException;
import com.example.backend.Repositories.RouteStationRepository;
import com.example.backend.Repositories.TrainRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrainService {
    private final TrainRepository trainRepository;
    private final RouteStationRepository routeStationRepository;

    public TrainService(TrainRepository trainRepository, RouteStationRepository routeStationRepository) {
        this.trainRepository = trainRepository;
        this.routeStationRepository = routeStationRepository;
    }

    public List<TrainSearchResultDTO> searchTrains(String from, String to) throws NotFoundException {
        List<TrainSearchResultDTO> results = trainRepository.findTrainsBetweenStations(from, to);

        if (results.isEmpty()) {
            throw new NotFoundException("Nu au fost găsite trenuri între " + from + " și " + to);
        }

        return results;
    }

    public List<JourneyDTO> findComplexRoute(String startNode, String endNode, int maxChanges) throws NotFoundException {
        List<JourneyDTO> foundJourneys = new ArrayList<>();
        Queue<List<TrainSegmentDTO>> queue = new LinkedList<>();

        for (Edge edge : getEdgesFrom(startNode)) {
            List<TrainSegmentDTO> initialPath = new ArrayList<>();
            initialPath.add(mapToSegment(edge, startNode));
            queue.add(initialPath);
        }

        while (!queue.isEmpty()) {
            List<TrainSegmentDTO> currentPath = queue.poll();
            TrainSegmentDTO lastSegment = currentPath.get(currentPath.size() - 1);
            String currentStation = lastSegment.toStation();

            if (currentStation.equalsIgnoreCase(endNode)) {
                foundJourneys.add(buildJourney(currentPath));
                continue;
            }

            if (routeStationRepository.isTargetOnSameRoute(lastSegment.trainNumber(), currentStation, endNode)) {
                continue;
            }

            if (currentPath.size() <= maxChanges) {
                LocalTime arrivalTimeAtChange = lastSegment.arrivalTime();

                for (Edge nextEdge : getEdgesFrom(currentStation)) {
                    if (isTimeValidAndReasonable(arrivalTimeAtChange, nextEdge.departure()) &&
                            !isStationInPath(nextEdge.toStation(), currentPath)) {

                        if (routeStationRepository.hasAlreadyPassedTarget(lastSegment.trainNumber(), lastSegment.fromStation(), currentStation, endNode)) {
                            continue;
                        }

                        List<TrainSegmentDTO> nextPath = new ArrayList<>(currentPath);
                        nextPath.add(mapToSegment(nextEdge, currentStation));
                        queue.add(nextPath);
                    }
                }
            }
        }

        if (foundJourneys.isEmpty()) {
            throw new NotFoundException("Nu s-a găsit nicio rută validă.");
        }

        return foundJourneys;
    }

    private boolean isTimeValidAndReasonable(LocalTime arrival, LocalTime departure) {
        long diff = java.time.Duration.between(arrival, departure).toMinutes();
        if (diff < 0) diff += 1440;

        return diff >= 15 && diff <= 240;
    }

    private JourneyDTO buildJourney(List<TrainSegmentDTO> path) {
        List<TrainSegmentDTO> segmentsWithWaitTimes = new ArrayList<>();

        for (int i = 0; i < path.size(); i++) {
            TrainSegmentDTO current = path.get(i);

            if (i > 0) {
                long diff = java.time.Duration.between(path.get(i-1).arrivalTime(), current.departureTime()).toMinutes();
                if (diff < 0) diff += 1440;
            }

            segmentsWithWaitTimes.add(new TrainSegmentDTO(
                    current.trainNumber(), current.fromStation(), current.toStation(),
                    current.departureTime(), current.arrivalTime()
            ));
        }

        return new JourneyDTO(segmentsWithWaitTimes);
    }

    @Cacheable("stationGraph")
    private List<Edge> getEdgesFrom(String stationName) {
        List<Object[]> results = routeStationRepository.findEdgesFromStation(stationName);
        return results.stream().map(row -> new Edge(
                String.valueOf(row[0]),
                String.valueOf(row[1]),
                convertToLocalTime(row[2]),
                convertToLocalTime(row[3]),
                ((Number) row[4]).intValue()
        )).toList();
    }

    private TrainSegmentDTO mapToSegment(Edge edge, String fromStationName) {
        return new TrainSegmentDTO(
                edge.trainNumber(), fromStationName, edge.toStation(),
                edge.departure(), edge.arrival()
        );
    }

    private boolean isStationInPath(String targetStation, List<TrainSegmentDTO> currentPath) {
        return currentPath.stream().anyMatch(s ->
                s.fromStation().equalsIgnoreCase(targetStation) || s.toStation().equalsIgnoreCase(targetStation));
    }

    private LocalTime convertToLocalTime(Object obj) {
        if (obj == null) return null;
        if (obj instanceof LocalTime) return (LocalTime) obj;
        if (obj instanceof java.sql.Time) return ((java.sql.Time) obj).toLocalTime();
        return LocalTime.parse(obj.toString());
    }
}
