package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.entity.ParticipationRequest;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long>, RequestRepositoryQueryDsl {
    List<ParticipationRequest> findAllByEventId(long eventId);

    List<ParticipationRequest> findAllByRequesterId(long userId);

    Optional<ParticipationRequest> findByEventIdAndRequesterId(long eventId, long requesterId);

    @Modifying
    @Query("UPDATE ParticipationRequest p set p.status = 'REJECT' where p.event.id = :id AND p.status = 'PENDING'")
    void rejectAllRequestByEventId(Long id);
}
