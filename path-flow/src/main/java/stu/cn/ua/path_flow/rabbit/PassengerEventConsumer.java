package stu.cn.ua.path_flow.rabbit;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import stu.cn.ua.path_flow.service.DetectionResult;
import stu.cn.ua.path_flow.service.PassengerDetectionService;

@Service
@RequiredArgsConstructor
public class PassengerEventConsumer {

    private final PassengerDetectionService detectionService;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @RabbitListener(queues = "passenger.coordinates.queue")
    public void consume(PassengerCoordinateEvent event) {

        Point point = geometryFactory.createPoint(
                new Coordinate(event.getLon(), event.getLat())
        );
        point.setSRID(4326);

        DetectionResult result = detectionService.detect(
                event.getPassengerEmail(),
                point,
                event.getDate(),
                event.getTime()
        );
    }
}

