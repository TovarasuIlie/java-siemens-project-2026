package com.example.backend.repository;

import com.example.backend.dto.TrainSearchResultDTO;
import com.example.backend.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainRepository extends JpaRepository<Train, Integer> {

    @Query(value = """
        SELECT new com.example.backend.DTOs.TrainSearchResultDTO(
            t.trainNumber,
            t.trainType,
            rs1.departureTime,
            rs2.arrivalTime,
            t.delayMinutes
        )
        FROM Train t
        JOIN RouteStation rs1 ON t.route.id = rs1.route.id
        JOIN RouteStation rs2 ON t.route.id = rs2.route.id
        WHERE rs1.station.name = :startStationName
          AND rs2.station.name = :endStationName
          AND rs1.stopOrder < rs2.stopOrder
        """)
    List<TrainSearchResultDTO> findTrainsBetweenStations(@Param("startStationName") String startStation, @Param("endStationName") String endStation);

    @Query(value = """
    SELECT 
        t1.train_number AS train1, s_change.name AS change_station,
        rs1_start.departure_time AS dep1, rs1_change.arrival_time AS arr1,
        t2.train_number AS train2, rs2_change.departure_time AS dep2, 
        rs2_end.arrival_time AS arr2
    FROM trains t1
    JOIN route_stations rs1_start ON t1.route_id = rs1_start.route_id
    JOIN route_stations rs1_change ON t1.route_id = rs1_change.route_id
    JOIN stations s_start ON rs1_start.station_id = s_start.id
    JOIN stations s_change ON rs1_change.station_id = s_change.id
    
    JOIN route_stations rs2_change ON rs2_change.station_id = s_change.id
    JOIN route_stations rs2_end ON rs2_change.route_id = rs2_end.route_id
    JOIN trains t2 ON t2.route_id = rs2_change.route_id
    JOIN stations s_end ON rs2_end.station_id = s_end.id
    
    WHERE s_start.name = :startName 
      AND s_end.name = :endName
      AND rs1_start.stop_order < rs1_change.stop_order
      AND rs2_change.stop_order < rs2_end.stop_order
      AND t1.id <> t2.id
    """, nativeQuery = true)
    List<Object[]> findIndirectConnections(String startName, String endName);
}
