package stu.cn.ua.path_flow.repository;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stu.cn.ua.path_flow.model.BusStop;

import java.util.Optional;

public interface BusStopRepository extends JpaRepository<BusStop, Long> {
    @Query(value = """
    SELECT *
    FROM bus_stop b
    WHERE ST_DWithin(
        geography(b.coordinates_bus_stop),
        geography(:point),
        :distance
    )
    ORDER BY ST_Distance(
        geography(b.coordinates_bus_stop),
        geography(:point)
    )
    LIMIT 1
""", nativeQuery = true)
    Optional<BusStop> findNearestWithinDistance(
            @Param("point") Point point,
            @Param("distance") double distance
    );

    @Query(
            value = """
            SELECT EXISTS (
                SELECT 1
                FROM bus_stop b
                WHERE ST_DWithin(b.coordinates_bus_stop, :point, :distance)
            )
        """,
            nativeQuery = true
    )
    boolean existsWithinDistance(
            @Param("point") Point point,
            @Param("distance") double distance
    );
}
