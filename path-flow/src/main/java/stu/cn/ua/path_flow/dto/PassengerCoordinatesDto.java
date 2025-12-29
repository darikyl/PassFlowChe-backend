package stu.cn.ua.path_flow.dto;

import lombok.Data;

@Data
public class PassengerCoordinatesDto {

    private Long id;

    private String coordinates_passenger;
    private String date_passenger;
    private String time_passenger;

    private String passengerState;

    private Long busStopId;
    private Long pathPartId;
}
