package com.example.backend.entity;

import com.example.backend.enums.TrainCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "trains")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "route")
@EqualsAndHashCode(exclude = "route")
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "train_number", nullable = false, unique = true)
    private String trainNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "train_type", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private TrainCategory trainType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

    @Column(name = "total_capacity", nullable = false)
    private Integer totalCapacity;

    @Column(name = "delay_minutes")
    private Integer delayMinutes = 0;
}
