package ru.practicum.repository;

import ru.practicum.entity.ParticipationRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestRepositoryQueryDsl {
    List<ParticipationRequest> findRequestsToNotify(LocalDateTime from, LocalDateTime to);

}
