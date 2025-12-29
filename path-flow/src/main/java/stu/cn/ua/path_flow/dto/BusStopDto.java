package stu.cn.ua.path_flow.dto;

import lombok.Data;
import stu.cn.ua.path_flow.model.BusStop;

@Data
public class BusStopDto {
    private Long id;
    private String name;
    private double latitude;
    private double longitude;

    public BusStopDto(BusStop entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        if (entity.getCoordinates() != null) {
            this.longitude = entity.getCoordinates().getX();
            this.latitude = entity.getCoordinates().getY();
        }
    }
}