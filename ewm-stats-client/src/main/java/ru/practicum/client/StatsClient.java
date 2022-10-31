package ru.practicum.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ru.practicum.Constants.DEFAULT_DATE_TIME_FORMAT;

@Slf4j
@Service
public class StatsClient {
    private final RestTemplate rest;
    private final String serverUrl;
    private final DateTimeFormatter formatter;
    private final String hitUri = "/hit";
    private final String statsUri = "/stats";

    public StatsClient(@Value("${stats-server.url}") String serverUrl,
                       RestTemplateBuilder builder,
                       ObjectMapper mapper
    ) {
        this.serverUrl = serverUrl;
        this.rest = builder.build();
        this.formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
    }

    public void hit(EndpointHit dto) {
        log.info("Sending dto with URI {} to stat server at {}", dto.getUri(), rest.getUriTemplateHandler());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EndpointHit> httpEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<String> response = rest.postForEntity(serverUrl + hitUri, httpEntity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Stats server write statistics");
        } else {
            log.warn("Something went wrong");
        }
    }

    public List<ViewStats> stats(List<String> uris) {
        log.info("Requesting stat for uris: {}", uris);
        LocalDateTime start = LocalDateTime.ofEpochSecond(0L, 0, ZoneOffset.UTC);
        LocalDateTime end = LocalDateTime.now().plusYears(1000);
        String url = buildStatUrl(start, end, uris);
        ResponseEntity<ViewStats[]> response = rest.getForEntity(url, ViewStats[].class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            log.info("Stats server returned statistics");
            return Arrays.asList(response.getBody());
        } else {
            log.info("Something went wrong");
            return Collections.emptyList();
        }
    }

    private String buildStatUrl(LocalDateTime start, LocalDateTime end, List<String> uris) {
        StringBuilder builder = new StringBuilder(serverUrl);
        builder
                .append(statsUri)
                .append("?")
                .append("start=").append(start.format(formatter))
                .append("&")
                .append("end=").append(end.format(formatter));
        if (uris != null && !uris.isEmpty()) {
            builder
                    .append("&")
                    .append("uris=");
            uris.forEach(
                    uri -> {
                        builder.append("&").append("uris=").append(uri);
                    });
        }
        return URLEncoder.encode(builder.toString(), StandardCharsets.UTF_8);
    }
}