package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.entity.ParticipationRequest;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
}
