package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.repository.StatisticEntity;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsService {
    private final StatsRepository repo;
    private final StatsMapper mapper;

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<ViewStats> stats;
        if (unique) {
            stats = repo.getAllUnique(start, end, uris);
            log.info("Received statistics for uris: {}; from: {}; to: {}. For unique visitors", uris, start, end);
        } else {
            stats = repo.getAll(start, end, uris);
        }
        return stats;
    }

    public void writeStats(EndpointHit dto) {
        StatisticEntity entity = mapper.dtoToEntity(dto);
        repo.save(entity);
        log.info("Statistics for uri {} saved", entity.getUri());
    }
}