package stu.cn.ua.path_flow.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import stu.cn.ua.path_flow.dto.PassengerCoordinatesDto;
import stu.cn.ua.path_flow.model.PassengerCoordinates;
import stu.cn.ua.path_flow.rabbit.PassengerCoordinateEvent;
import stu.cn.ua.path_flow.rabbit.PassengerEventProducer;
import stu.cn.ua.path_flow.repository.PassengerCoordinatesRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PassengerCoordinatesService {

    private final PassengerCoordinatesRepository passengerRepository;
    private final PassengerDetectionService detectionService;
    private final PassengerEventProducer eventProducer;

    private final WKTReader wktReader = new WKTReader();

    public PassengerCoordinatesDto addPassenger(
            PassengerCoordinatesDto dto,
            String email
    ) {
        try {
            Point point = (Point) wktReader.read(dto.getCoordinates_passenger());
            point.setSRID(4326);

            LocalDate date = LocalDate.parse(dto.getDate_passenger());
            LocalTime time = LocalTime.parse(dto.getTime_passenger());

            DetectionResult detection =
                    detectionService.detect(email, point, date, time);

            eventProducer.send(
                    PassengerCoordinateEvent.builder()
                            .passengerEmail(email)
                            .lon(point.getX())
                            .lat(point.getY())
                            .date(date)
                            .time(time)
                            .build()
            );

            PassengerCoordinates entity =
                    PassengerCoordinates.builder()
                            .coordinates(point)
                            .date(date)
                            .time(time)
                            .passengerEmail(email)
                            .passengerState(detection.getState())
                            .busStop(detection.getBusStop())
                            .pathPart(detection.getPathPart())
                            .build();

            PassengerCoordinates saved =
                    passengerRepository.save(entity);

            dto.setId(saved.getId());
            dto.setPassengerState(saved.getPassengerState().name());

            dto.setBusStopId(
                    saved.getBusStop() != null
                            ? saved.getBusStop().getId()
                            : null
            );

            dto.setPathPartId(
                    saved.getPathPart() != null
                            ? saved.getPathPart().getId()
                            : null
            );

            return dto;

        } catch (Exception e) {
            throw new RuntimeException("Failed to save passenger coordinates", e);
        }
    }


    public long countUniquePassengersByBusStopLast30Sec(Long busStopId) {
        return passengerRepository
                .findByBusStop_IdAndDateAndTimeAfter(
                        busStopId,
                        LocalDate.now(),
                        LocalTime.now().minusSeconds(30)
                )
                .stream()
                .map(PassengerCoordinates::getPassengerEmail)
                .distinct()
                .count();
    }

    public Map<Long, Long> countUniquePassengersByPathPartLast30Sec() {
        return passengerRepository
                .findByDateAndTimeAfter(
                        LocalDate.now(),
                        LocalTime.now().minusSeconds(30)
                )
                .stream()
                .filter(pc -> pc.getPathPart() != null)
                .collect(Collectors.groupingBy(
                        pc -> pc.getPathPart().getId(),
                        Collectors.mapping(
                                PassengerCoordinates::getPassengerEmail,
                                Collectors.collectingAndThen(
                                        Collectors.toSet(),
                                        set -> (long) set.size()
                                )
                        )
                ));
    }
}
