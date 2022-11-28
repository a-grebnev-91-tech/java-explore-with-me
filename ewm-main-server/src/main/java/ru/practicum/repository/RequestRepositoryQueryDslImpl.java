package ru.practicum.repository;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import ru.practicum.entity.ParticipationRequest;
import ru.practicum.model.EventState;
import ru.practicum.model.RequestStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.entity.QParticipationRequest.participationRequest;

@RequiredArgsConstructor
public class RequestRepositoryQueryDslImpl implements RequestRepositoryQueryDsl {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<ParticipationRequest> findRequestsToNotify(LocalDateTime from, LocalDateTime to) {
        JPAQuery<ParticipationRequest> jpaQuery = new JPAQuery<>(em);
        jpaQuery.from(participationRequest)
                .where(participationRequest.status.eq(RequestStatus.CONFIRMED)
                        .and(participationRequest.event.state.eq(EventState.PUBLISHED)
                                .and(participationRequest.event.eventDate.between(from, to))));
        return jpaQuery.fetch();
    }
}
