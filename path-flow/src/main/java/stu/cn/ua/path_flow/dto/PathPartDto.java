package stu.cn.ua.path_flow.dto;

import lombok.Data;
import stu.cn.ua.path_flow.model.PathPart;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Data
public class PathPartDto {

    private Long id;
    private String namePath;

    private List<List<double[]>> multiLineCoordinates;

    private Double pointLon;
    private Double pointLat;

    public PathPartDto(PathPart entity) {
        this.id = entity.getId();
        this.namePath = entity.getNamePath();

        if (entity.getPath() != null) {
            MultiLineString mls = entity.getPath();

            this.multiLineCoordinates =
                    IntStream.range(0, mls.getNumGeometries())
                            .mapToObj(i -> (LineString) mls.getGeometryN(i))
                            .map(ls ->
                                    Arrays.stream(ls.getCoordinates())
                                            .map(c -> new double[]{c.getX(), c.getY()})
                                            .toList()
                            )
                            .toList();
        }

        if (entity.getTextLabel() != null) {
            this.pointLon = entity.getTextLabel().getX();
            this.pointLat = entity.getTextLabel().getY();
        }
    }
}
