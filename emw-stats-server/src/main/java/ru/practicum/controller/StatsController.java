package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.StatsService;

import java.util.List;

import static ru.practicum.util.Constants.HIT_API_PREFIX;
import static ru.practicum.util.Constants.STATS_API_PREFIX;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service;

    @GetMapping(STATS_API_PREFIX)
    public List<ViewStats> getStats(
            @RequestParam("start") String start,
            @RequestParam("end") String end,
            @RequestParam(value = "uris", required = false) List<String> uris,
            @RequestParam(value = "unique", defaultValue = "false") boolean unique
    ) {
        log.info("Requesting stats for uris {} for period from {} to {}", uris, start, end);
        return service.getStats(start, end, uris, unique);
    }

    @PostMapping(HIT_API_PREFIX)
    public void writeStats(@RequestBody EndpointHit dto) {
        log.info("Writing statistics for app {}, uri {}", dto.getApp(), dto.getUri());
        service.writeStats(dto);
    }
}
