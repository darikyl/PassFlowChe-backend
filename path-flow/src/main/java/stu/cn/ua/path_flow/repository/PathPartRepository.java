package stu.cn.ua.path_flow.repository;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stu.cn.ua.path_flow.model.PathPart;

import java.util.Optional;

public interface PathPartRepository extends JpaRepository<PathPart, Long> {

    @Query(
            value = """
            SELECT EXISTS (
                SELECT 1
                FROM path_part p
                WHERE ST_DWithin(p.path, :point, :distance)
            )
        """,
            nativeQuery = true
    )
    boolean existsWithinDistance(
            @Param("point") Point point,
            @Param("distance") double distance
    );

    @Query(
            value = """
            SELECT *
            FROM path_part p
            WHERE ST_DWithin(p.path, :point, :distance)
            LIMIT 1
        """,
            nativeQuery = true
    )
    Optional<PathPart> findFirstWithinDistance(
            @Param("point") Point point,
            @Param("distance") double distance
    );


}

