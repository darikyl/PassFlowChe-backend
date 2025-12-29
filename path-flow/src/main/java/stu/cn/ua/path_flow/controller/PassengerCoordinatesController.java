package stu.cn.ua.path_flow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stu.cn.ua.path_flow.dto.PassengerCoordinatesDto;
import stu.cn.ua.path_flow.service.PassengerCoordinatesService;

import java.util.Map;

@RestController
@RequestMapping("/path-flow/passengers")
@RequiredArgsConstructor
public class PassengerCoordinatesController {

    private final PassengerCoordinatesService service;

    @PostMapping("/addPassenger")
    public ResponseEntity<PassengerCoordinatesDto> addPassenger(
            @RequestHeader("X-User-Email") String userEmail,
            @RequestBody PassengerCoordinatesDto passenger
    ) {
        return ResponseEntity.ok(
                service.addPassenger(passenger, userEmail)
        );
    }


    @GetMapping("/bus-stop/{busStopId}/count")
    public ResponseEntity<Long> countByBusStop(@PathVariable Long busStopId) {
        return ResponseEntity.ok(
                service.countUniquePassengersByBusStopLast30Sec(busStopId)
        );
    }

    @GetMapping("/path-parts/count")
    public ResponseEntity<Map<Long, Long>> countByPathParts() {
        return ResponseEntity.ok(
                service.countUniquePassengersByPathPartLast30Sec()
        );
    }
}

