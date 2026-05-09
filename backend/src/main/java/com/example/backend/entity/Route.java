package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "stations")
@EqualsAndHashCode(exclude = "stations")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "route_name", nullable = false)
    private String routeName;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    @OrderBy("stopOrder ASC")
    private List<RouteStation> stations;
}
