package stu.cn.ua.path_flow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stu.cn.ua.path_flow.dto.BusStopDto;
import stu.cn.ua.path_flow.service.BusStopService;

import java.util.List;

@RestController
@RequestMapping("/path-flow")
@RequiredArgsConstructor
public class BusStopController {

    private final BusStopService service;

    @GetMapping("/allBusStops")
    public ResponseEntity<List<BusStopDto>> getAllStops(
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {

        return ResponseEntity.ok(service.getAllStops());
    }
}
