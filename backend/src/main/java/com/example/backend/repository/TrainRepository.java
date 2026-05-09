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
        SELECT new com.example.backend.dto.TrainSearchResultDTO(
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
}
