package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.entity.Event;
import ru.practicum.util.OffsetPageable;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByInitiatorId(long initiatorId, OffsetPageable pageable);

    boolean existsByCategoryId(long categoryId);
}
