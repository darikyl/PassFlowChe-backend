package stu.cn.ua.path_flow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;


@Entity
@Table(name = "bus_stop")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bus_stop")
    private Long id;

    @Column(name = "coordinates_bus_stop", columnDefinition = "GEOMETRY(POINT, 4326)")
    private Point coordinates;

    @Column(name = "name_bus_stop", length = 255)
    private String name;
}