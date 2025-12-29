package stu.cn.ua.path_flow.model;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "path_part")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PathPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_path_part")
    private Long id;

    @Column(name = "path", columnDefinition = "GEOMETRY(MULTILINESTRING, 4326)")
    private MultiLineString path;

    @Column(name = "text_label", columnDefinition = "GEOMETRY(POINT, 4326)")
    private Point textLabel;

    @Column(name = "name_path", length = 255)
    private String namePath;
}
