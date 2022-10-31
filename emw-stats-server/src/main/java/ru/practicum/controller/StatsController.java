package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.StatsService;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.Constants.DEFAULT_DATE_TIME_FORMAT;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service;

    @GetMapping("/stats")
    public List<ViewStats> getStats(
            @RequestParam("start") String start,
            @RequestParam("end") String end,
            @RequestParam(value = "uris", required = false) List<String> uris,
            @RequestParam(value = "unique", defaultValue = "false") boolean unique
    ) {
        log.info("Requesting stats for uris {} for period from {} to {}", uris, start, end);
        return service.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    public void writeStats(@RequestBody EndpointHit dto) {
        log.info("Writing statistics for app {}, uri {}", dto.getApp(), dto.getUri());
        service.writeStats(dto);
    }
}
