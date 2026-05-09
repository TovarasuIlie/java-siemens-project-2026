package com.example.backend.Repositories;

import com.example.backend.Entities.RouteStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RouteStationRepository extends JpaRepository<RouteStation, Integer> {

    @Query(value = """
        SELECT
            t.train_number,
            s_to.name        AS destination_station,
            rs_from.departure_time,
            rs_to.arrival_time,
            t.route_id,
            rs_from.stop_order   AS from_stop_order,
            rs_to.stop_order     AS to_stop_order
        FROM route_stations rs_from
        JOIN stations s_from ON rs_from.station_id = s_from.id
        JOIN route_stations rs_to  ON rs_from.route_id = rs_to.route_id
        JOIN stations s_to         ON rs_to.station_id = s_to.id
        JOIN trains t              ON t.route_id = rs_from.route_id
        WHERE s_from.name = :stationName
          AND rs_from.stop_order < rs_to.stop_order
        """, nativeQuery = true)
    List<Object[]> findEdgesFromStation(@Param("stationName") String stationName);

    @Query(value = """
    SELECT s.name
    FROM route_stations rs
    JOIN stations s ON rs.station_id = s.id
    JOIN trains t ON t.route_id = rs.route_id
    WHERE t.train_number = :trainNumber
      AND rs.stop_order >= :fromStopOrder
      AND rs.stop_order <= :toStopOrder
    """, nativeQuery = true)
    List<String> findIntermediateStations(
            @Param("trainNumber") String trainNumber,
            @Param("fromStopOrder") int fromStopOrder,
            @Param("toStopOrder") int toStopOrder
    );
}
