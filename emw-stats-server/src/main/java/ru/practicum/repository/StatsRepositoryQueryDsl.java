package ru.practicum.repository;

import ru.practicum.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

//TODO rm
public interface StatsRepositoryQueryDsl {
    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
