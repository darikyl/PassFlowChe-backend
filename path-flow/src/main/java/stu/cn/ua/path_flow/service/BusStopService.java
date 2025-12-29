package stu.cn.ua.path_flow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stu.cn.ua.path_flow.dto.BusStopDto;
import stu.cn.ua.path_flow.repository.BusStopRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusStopService {
    private final BusStopRepository repository;

    public List<BusStopDto> getAllStops() {
        return repository.findAll()
                .stream()
                .map(BusStopDto::new)
                .toList();
    }
}