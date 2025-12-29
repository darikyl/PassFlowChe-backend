package stu.cn.ua.path_flow.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import stu.cn.ua.path_flow.model.BusStop;
import stu.cn.ua.path_flow.model.PathPart;
import stu.cn.ua.path_flow.model.PassengerState;

@Data
@AllArgsConstructor
public class DetectionResult {
    private PassengerState state;
    private BusStop busStop;
    private PathPart pathPart;
}
