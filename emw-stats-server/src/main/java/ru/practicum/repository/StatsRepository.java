package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.controller.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<StatisticEntity, Long> {
    @Query(
            "SELECT new ru.practicum.controller.ViewStats (e.app, e.uri, count(e)) " +
                    "FROM StatisticEntity e " +
                    "WHERE e.timestamp >= :start " +
                    "AND e.timestamp <= :end " +
                    "AND (:uris IS NULL OR e.uri IN :uris) " +
                    "GROUP BY e.app, e.uri"
    )
    List<ViewStats> getAll(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );

    @Query(
            "SELECT new ru.practicum.controller.ViewStats (e.app, e.uri, count(e)) " +
                    "FROM StatisticEntity e " +
                    "WHERE e.timestamp >= :start " +
                    "AND e.timestamp <= :end " +
                    "AND (:uris IS NULL OR e.uri IN :uris) " +
                    "GROUP BY e.app, e.uri, e.ip"
    )
    List<ViewStats> getAllUnique(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );
}
