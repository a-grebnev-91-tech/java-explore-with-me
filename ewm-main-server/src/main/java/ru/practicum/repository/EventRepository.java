package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Modifying
    @Query("UPDATE ParticipationRequest p set p.status = 'REJECT' where p.event.id = :id AND p.status = 'PENDING'")
    void rejectAllRequestByEventId(Long id);
}
