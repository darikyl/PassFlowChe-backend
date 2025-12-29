package stu.cn.ua.path_flow.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import stu.cn.ua.path_flow.model.*;
import stu.cn.ua.path_flow.repository.BusStopRepository;
import stu.cn.ua.path_flow.repository.PassengerCoordinatesRepository;
import stu.cn.ua.path_flow.repository.PathPartRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PassengerDetectionService {

    private final PassengerCoordinatesRepository passengerRepository;
    private final BusStopRepository busStopRepository;
    private final PathPartRepository pathPartRepository;


    private static final double BUS_STOP_RADIUS_METERS = 0.00009;
    private static final double ROAD_RADIUS_METERS = 0.000050;

    private static final int BUS_STOP_WINDOW_SECONDS = 30;
    private static final int BUS_STOP_REQUIRED_HITS = 1;

    private static final double WALK_MIN = 4;
    private static final double WALK_MAX = 6;
    private static final double TRANSPORT_MIN = 10;
    private static final double TRANSPORT_MAX = 15;

    public DetectionResult detect(
            String email,
            Point point,
            LocalDate date,
            LocalTime time
    ) {

        PassengerState lastState = getLastState(email, date, time);

        Optional<BusStop> busStopOpt =
                busStopRepository.findNearestWithinDistance(point, BUS_STOP_RADIUS_METERS);

        if (busStopOpt.isPresent()) {

            BusStop stop = busStopOpt.get();

            boolean confirmed =
                    wasNearBusStopLast30Sec(email, stop.getCoordinates(), date, time);

            if (confirmed) {
                return new DetectionResult(
                        PassengerState.NEAR_BUS_STOP,
                        stop,
                        null
                );
            }

            return new DetectionResult(PassengerState.UNKNOWN, null, null);
        }

        Optional<PathPart> pathPartOpt =
                pathPartRepository.findFirstWithinDistance(point, ROAD_RADIUS_METERS);

        if (pathPartOpt.isEmpty()) {
            return new DetectionResult(PassengerState.UNKNOWN, null, null);
        }

        PathPart pathPart = pathPartOpt.get();

        PassengerState state =
                analyzeSpeedWithStateMachine(email, date, time, lastState);

        return new DetectionResult(state, null, pathPart);
    }

    private PassengerState analyzeSpeedWithStateMachine(
            String email,
            LocalDate date,
            LocalTime time,
            PassengerState lastState
    ) {

        List<PassengerCoordinates> history =
                passengerRepository.findByPassengerEmailAndDateAndTimeAfter(
                        email,
                        date,
                        time.minusSeconds(90)
                );

        if (history.size() < 2) {
            return PassengerState.NEAR_ROAD;
        }

        int slowHits = 0;

        for (int i = 1; i < history.size(); i++) {

            double meters =
                    passengerRepository.distanceMeters(
                            history.get(i).getCoordinates(),
                            history.get(i - 1).getCoordinates()
                    );

            double speedKmH = (meters / 10.0) * 3.6;

            if (speedKmH >= TRANSPORT_MIN && speedKmH <= TRANSPORT_MAX) {
                return PassengerState.PASSENGER;
            }

            if (speedKmH >= WALK_MIN && speedKmH <= WALK_MAX) {
                slowHits++;
            } else {
                slowHits = 0;
            }

            if (
                    slowHits >= 6 &&
                            lastState == PassengerState.PASSENGER
            ) {
                return PassengerState.LEFT_TRANSPORT;
            }
        }

        return PassengerState.NEAR_ROAD;
    }

    private boolean wasNearBusStopLast30Sec(
            String email,
            Point busStopPoint,
            LocalDate date,
            LocalTime time
    ) {

        List<PassengerCoordinates> history =
                passengerRepository.findByPassengerEmailAndDateAndTimeAfter(
                        email,
                        date,
                        time.minusSeconds(BUS_STOP_WINDOW_SECONDS)
                );

        int hits = 0;

        for (PassengerCoordinates pc : history) {
            double meters =
                    passengerRepository.distanceMeters(
                            pc.getCoordinates(),
                            busStopPoint
                    );

            if (meters <= BUS_STOP_RADIUS_METERS) {
                hits++;
            }

            if (hits >= BUS_STOP_REQUIRED_HITS) {
                return true;
            }
        }

        return false;
    }

    private PassengerState getLastState(
            String email,
            LocalDate date,
            LocalTime time
    ) {
        return passengerRepository
                .findFirstByPassengerEmailAndDateAndTimeBeforeOrderByTimeDesc(
                        email, date, time
                )
                .map(PassengerCoordinates::getPassengerState)
                .orElse(PassengerState.UNKNOWN);
    }
}
