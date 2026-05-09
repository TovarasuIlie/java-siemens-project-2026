package com.example.backend.entity;

import com.example.backend.enums.TrainCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trains")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "train_number", nullable = false, unique = true)
    private String trainNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "train_type", nullable = false)
    private TrainCategory trainType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

    @Column(name = "total_capacity", nullable = false)
    private Integer totalCapacity;

    @Column(name = "delay_minutes")
    private Integer delayMinutes = 0;
}
