package com.example.backend.Services;

import com.example.backend.DTOs.*;
import com.example.backend.Exeptions.NotFoundException;
import com.example.backend.Repositories.RouteStationRepository;
import com.example.backend.Repositories.TrainRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
        PriorityQueue<ScoredPath> pq = new PriorityQueue<>();

        for (Edge edge : getEdgesFrom(startNode)) {
            TrainSegmentDTO firstSegment = mapToSegment(edge, startNode);
            long initialCost = segmentDuration(edge.departure(), edge.arrival());

            pq.add(new ScoredPath(
                    List.of(firstSegment),
                    edge.departure(),
                    initialCost
            ));
        }

        while (!pq.isEmpty() && foundJourneys.size() < 20) {
            ScoredPath current = pq.poll();
            List<TrainSegmentDTO> currentPath = current.path();
            TrainSegmentDTO lastSegment = currentPath.get(currentPath.size() - 1);
            String currentStation = lastSegment.toStation();

            if (currentStation.equalsIgnoreCase(endNode)) {
                foundJourneys.add(buildJourney(currentPath));
                continue;
            }

            if (currentPath.size() > maxChanges) {
                continue;
            }

            Set<String> visitedStations = buildVisitedSet(currentPath);

            for (Edge nextEdge : getEdgesFrom(currentStation)) {

                if (!isTransferValid(lastSegment.arrivalTime(), nextEdge.departure())) {
                    continue;
                }

                if (visitedStations.contains(nextEdge.toStation().toLowerCase())) {
                    continue;
                }

                if (sameTrainHasPassedTarget(lastSegment, nextEdge, endNode)) {
                    continue;
                }

                long waitTime = waitDuration(lastSegment.arrivalTime(), nextEdge.departure());
                long segmentTime = segmentDuration(nextEdge.departure(), nextEdge.arrival());
                long newCost = current.gCost() + waitTime + segmentTime;

                List<TrainSegmentDTO> nextPath = new ArrayList<>(currentPath);
                nextPath.add(mapToSegment(nextEdge, currentStation));

                pq.add(new ScoredPath(nextPath, current.firstDeparture(), newCost));
            }
        }

        if(foundJourneys.isEmpty()) {
            throw new NotFoundException("Nu au fost găsite trenuri între " + startNode + " și " + endNode);
        }

        return deduplicateJourneys(foundJourneys);
    }

    private boolean sameTrainHasPassedTarget(TrainSegmentDTO lastSegment, Edge nextEdge, String endNode) throws NotFoundException {
        if (!lastSegment.trainNumber().equals(nextEdge.trainNumber())) {
            return false;
        }

        return getEdgesFrom(lastSegment.toStation()).stream()
                .filter(e -> e.trainNumber().equals(nextEdge.trainNumber()))
                .filter(e -> e.toStation().equalsIgnoreCase(endNode))
                .anyMatch(e -> e.toStopOrder() < nextEdge.fromStopOrder());
    }

    private Set<String> buildVisitedSet(List<TrainSegmentDTO> path) {
        Set<String> visited = new HashSet<>();

        for (TrainSegmentDTO seg : path) {
            visited.add(seg.fromStation().toLowerCase());
            visited.add(seg.toStation().toLowerCase());

            routeStationRepository.findIntermediateStations(
                            seg.trainNumber(),
                            seg.fromStopOrder(),
                            seg.toStopOrder()
                    )
                    .forEach(s -> visited.add(s.toLowerCase()));
        }

        return visited;
    }

    private boolean isTransferValid(LocalTime arrival, LocalTime departure) {
        long diff = Duration.between(arrival, departure).toMinutes();
        if (diff < 0) diff += 1440;
        return diff >= 15 && diff <= 240;
    }

    private long segmentDuration(LocalTime departure, LocalTime arrival) {
        long diff = Duration.between(departure, arrival).toMinutes();
        if (diff <= 0) diff += 1440;
        return diff;
    }

    private long waitDuration(LocalTime arrival, LocalTime departure) {
        long diff = Duration.between(arrival, departure).toMinutes();
        if (diff < 0) diff += 1440;
        return diff;
    }

    private List<Edge> getEdgesFrom(String stationName) throws NotFoundException {
        List<Object[]> results = routeStationRepository.findEdgesFromStation(stationName);

        if(results.isEmpty()) {
            throw new NotFoundException("Statia " + stationName + " nu a fost gasita!");
        }

        return results.stream().map(row -> new Edge(
                String.valueOf(row[0]),
                String.valueOf(row[1]),
                convertToLocalTime(row[2]),
                convertToLocalTime(row[3]),
                ((Number) row[4]).intValue(),
                ((Number) row[5]).intValue(),
                ((Number) row[6]).intValue()
        )).toList();
    }

    private TrainSegmentDTO mapToSegment(Edge edge, String fromStation) {
        return new TrainSegmentDTO(
                edge.trainNumber(),
                fromStation,
                edge.toStation(),
                edge.departure(),
                edge.arrival(),
                edge.fromStopOrder(),
                edge.toStopOrder()
        );
    }

    private JourneyDTO buildJourney(List<TrainSegmentDTO> path) {
        return new JourneyDTO(new ArrayList<>(path));
    }

    private LocalTime convertToLocalTime(Object obj) {
        if (obj == null) return null;
        if (obj instanceof LocalTime lt) return lt;
        if (obj instanceof java.sql.Time t) return t.toLocalTime();
        return LocalTime.parse(obj.toString());
    }

    private List<JourneyDTO> deduplicateJourneys(List<JourneyDTO> journeys) {
        if (journeys == null || journeys.isEmpty()) return new ArrayList<>();

        return journeys.stream()
                .collect(Collectors.toMap(
                        j -> j.segments().stream()
                                .map(TrainSegmentDTO::trainNumber)
                                .collect(Collectors.joining("-")),
                        j -> j,
                        (existing, replacement) ->
                                calculateTotalPathTime(existing.segments()) <=
                                        calculateTotalPathTime(replacement.segments())
                                        ? existing : replacement
                ))
                .values().stream()
                .sorted(Comparator.comparing(j -> j.segments().get(0).departureTime()))
                .toList();
    }

    private long calculateTotalPathTime(List<TrainSegmentDTO> segments) {
        if (segments == null || segments.isEmpty()) return 0;
        long total = Duration.between(
                segments.get(0).departureTime(),
                segments.get(segments.size() - 1).arrivalTime()
        ).toMinutes();
        return total <= 0 ? total + 1440 : total;
    }
}
