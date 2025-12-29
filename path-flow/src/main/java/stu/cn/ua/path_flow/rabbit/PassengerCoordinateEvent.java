package stu.cn.ua.path_flow.rabbit;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerCoordinateEvent {

    private String passengerEmail;
    private double lon;
    private double lat;

    private LocalDate date;
    private LocalTime time;

    private String passengerState;
}
