package com.example.backend.Repositories;

import com.example.backend.Entities.RouteStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteStationRepository extends JpaRepository<RouteStation, Integer> {

    @Query(value = """
        SELECT 
            t.train_number, 
            s_to.name AS destination_station, 
            rs_from.departure_time, 
            rs_to.arrival_time, 
            t.route_id
        FROM route_stations rs_from
        JOIN stations s_from ON rs_from.station_id = s_from.id
        JOIN route_stations rs_to ON rs_from.route_id = rs_to.route_id
        JOIN stations s_to ON rs_to.station_id = s_to.id
        JOIN trains t ON t.route_id = rs_from.route_id
        WHERE s_from.name = :stationName
          AND rs_from.stop_order < rs_to.stop_order
        """, nativeQuery = true)
    List<Object[]> findEdgesFromStation(@Param("stationName") String stationName);

    @Query(value = """
    SELECT COUNT(*) > 0 
    FROM route_stations rs1
    JOIN route_stations rs2 ON rs1.route_id = rs2.route_id
    JOIN trains t ON t.route_id = rs1.route_id
    WHERE t.train_number = :trainNumber
      AND rs1.station_id = (SELECT id FROM stations WHERE name = :currentStation)
      AND rs2.station_id = (SELECT id FROM stations WHERE name = :targetStation)
      AND rs1.stop_order < rs2.stop_order
    """, nativeQuery = true)
    boolean isTargetOnSameRoute(String trainNumber, String currentStation, String targetStation);

    @Query(value = """
    SELECT COUNT(*) > 0 
    FROM route_stations rs_dest
    JOIN route_stations rs_change ON rs_dest.route_id = rs_change.route_id
    JOIN trains t ON t.route_id = rs_dest.route_id
    WHERE t.train_number = :trainNumber
      AND rs_dest.station_id = (SELECT id FROM stations WHERE name = :endNode)
      AND rs_change.station_id = (SELECT id FROM stations WHERE name = :currentStation)
      AND rs_dest.stop_order < rs_change.stop_order
    """, nativeQuery = true)
    boolean hasAlreadyPassedTarget(String trainNumber, String fromStation, String currentStation, String endNode);
}
