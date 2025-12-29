package stu.cn.ua.path_flow.repository;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stu.cn.ua.path_flow.model.PassengerCoordinates;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
public interface PassengerCoordinatesRepository
        extends JpaRepository<PassengerCoordinates, Long> {

    List<PassengerCoordinates>
    findByPassengerEmailAndDateAndTimeAfter(
            String email,
            LocalDate date,
            LocalTime time
    );

    List<PassengerCoordinates>
    findByBusStop_IdAndDateAndTimeAfter(
            Long busStopId,
            LocalDate date,
            LocalTime time
    );

    Optional<PassengerCoordinates>
    findFirstByPassengerEmailAndDateAndTimeBeforeOrderByTimeDesc(
            String passengerEmail,
            LocalDate date,
            LocalTime time
    );

    List<PassengerCoordinates>
    findByDateAndTimeAfter(
            LocalDate date,
            LocalTime time
    );
    @Query(
            value = """
        SELECT ST_DistanceSphere(:p1, :p2)
    """,
            nativeQuery = true
    )
    double distanceMeters(
            @Param("p1") Point p1,
            @Param("p2") Point p2
    );
}
