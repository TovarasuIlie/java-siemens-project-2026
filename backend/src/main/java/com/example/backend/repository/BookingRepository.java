package com.example.backend.repository;

import com.example.backend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("""
        SELECT COALESCE(SUM(b.seatsBooked), 0)
        FROM Booking b
        WHERE b.train.id = :trainId
        AND b.travelDate = :travelDate
    """)
    Integer sumBookedSeatsForTrainOnDate(
            @Param("trainId") Integer trainId,
            @Param("travelDate") LocalDate travelDate
    );

    List<Booking> findByTrain_Id(Integer trainId);

    @Query("""
        SELECT b FROM Booking b
        WHERE b.train.id = :trainId
        AND b.travelDate >= :today
    """)
    List<Booking> findUpcomingBookingsByTrainId(
            @Param("trainId") Integer trainId,
            @Param("today") LocalDate today
    );

    List<Booking> findByUser_Email(String email);
}
