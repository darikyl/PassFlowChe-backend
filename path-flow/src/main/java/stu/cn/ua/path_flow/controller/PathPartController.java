package stu.cn.ua.path_flow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stu.cn.ua.path_flow.dto.PathPartDto;
import stu.cn.ua.path_flow.service.PathPartService;

import java.util.List;

@RestController
@RequestMapping("/path-flow")
@RequiredArgsConstructor
public class PathPartController {

    private final PathPartService service;

    @GetMapping("/allPathParts")
    public ResponseEntity<List<PathPartDto>> getAllPathParts(
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {

        return ResponseEntity.ok(service.getAllPathParts());
    }
}