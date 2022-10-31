package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.repository.StatisticEntity;
import ru.practicum.repository.StatsRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.Constants.DEFAULT_DATE_TIME_FORMAT;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsService {
    private final StatsRepository repo;
    private final StatsMapper mapper;

    public List<ViewStats> getStats(String start, String end, List<String> uris, boolean unique) {
        start = URLDecoder.decode(start, StandardCharsets.UTF_8);
        end = URLDecoder.decode(end, StandardCharsets.UTF_8);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);
        List<ViewStats> stats;
        if (unique) {
            stats = repo.getAllUnique(startDate, endDate, uris);
            log.info("Received statistics for uris: {}; from: {}; to: {}. For unique visitors", uris, start, end);
        } else {
            stats = repo.getAll(startDate, endDate, uris);
            log.info("Received all statistics for uris: {}; from: {}; to: {}.", uris, start, end);
        }
        return stats;
    }

    public void writeStats(EndpointHit dto) {
        StatisticEntity entity = mapper.dtoToEntity(dto);
        repo.save(entity);
        log.info("Statistics for uri {} saved", entity.getUri());
    }
}