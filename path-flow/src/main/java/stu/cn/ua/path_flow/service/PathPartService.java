package stu.cn.ua.path_flow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stu.cn.ua.path_flow.dto.PathPartDto;
import stu.cn.ua.path_flow.repository.PathPartRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PathPartService {
    private final PathPartRepository repository;

    public List<PathPartDto> getAllPathParts() {
        return repository.findAll()
                .stream()
                .map(PathPartDto::new)
                .toList();
    }
}