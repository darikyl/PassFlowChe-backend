package stu.cn.ua.path_flow.model;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "passenger_coordinates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerCoordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_passenger")
    private Long id;

    @Column(
            name = "coordinates_passenger",
            columnDefinition = "geometry(Point,4326)"
    )
    private Point coordinates;

    @Column(name = "date_passenger", nullable = false)
    private LocalDate date;

    @Column(name = "time_passenger", nullable = false)
    private LocalTime time;

    @Column(name = "passenger_email", nullable = false)
    private String passengerEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "passenger_state", nullable = false)
    private PassengerState passengerState;

    @ManyToOne
    @JoinColumn(name = "bus_stop_passenger")
    private BusStop busStop;

    @ManyToOne
    @JoinColumn(name = "path_part_passenger")
    private PathPart pathPart;
}
