package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.entity.Event;
import ru.practicum.model.EventState;
import ru.practicum.util.OffsetPageable;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryQueryDsl {
    Optional<Event> findByIdAndState(long id, EventState published);

    List<Event> findByInitiatorId(long initiatorId, OffsetPageable pageable);

    boolean existsByCategoryId(long categoryId);
}
