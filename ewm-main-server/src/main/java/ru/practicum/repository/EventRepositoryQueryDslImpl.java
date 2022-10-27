package ru.practicum.repository;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import ru.practicum.entity.Event;
import ru.practicum.model.EventOrderBy;
import ru.practicum.model.EventState;
import ru.practicum.util.AdminEventParamObj;
import ru.practicum.util.PublicEventParamObj;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.entity.QEvent.event;

@RequiredArgsConstructor
public class EventRepositoryQueryDslImpl implements EventRepositoryQueryDsl {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Event> findAllByPublicParams(PublicEventParamObj paramObj) {
        JPAQuery<Event> jpaQuery = new JPAQuery<>(entityManager);
        jpaQuery = jpaQuery.from(event).where(event.state.eq(EventState.PUBLISHED));
        if (paramObj.hasText())
            applyTextFilter(paramObj.getText(), jpaQuery);
        if (paramObj.hasCategories())
            applyCategoriesFilter(paramObj.getCategories(), jpaQuery);
        if (paramObj.hasPaid())
            applyPaidFilter(paramObj.getPaid(), jpaQuery);
        if (paramObj.hasOnlyAvailable())
            applyOnlyAvailableFilter(paramObj.getOnlyAvailable(), jpaQuery);
        if (paramObj.hasOrderBy())
            applyOrderByFilter(paramObj.getOrderBy(), jpaQuery);
        applyDateRangeFilter(paramObj.getRangeStart(), paramObj.getRangeEnd(), jpaQuery);
        jpaQuery.offset(paramObj.getOffset()).limit(paramObj.getSize());
        return jpaQuery.fetch();
    }

    @Override
    public List<Event> findAllByAdminParams(AdminEventParamObj paramObj) {
        JPAQuery<Event> jpaQuery = new JPAQuery<>(entityManager);
        jpaQuery = jpaQuery.from(event);
        if (paramObj.hasUsers())
            applyUsersFilter(paramObj.getUsers(), jpaQuery);
        if (paramObj.hasStates())
            applyStatesFilter(paramObj.getStates(), jpaQuery);
        if (paramObj.hasCategories())
            applyCategoriesFilter(paramObj.getCategories(), jpaQuery);
        applyDateRangeFilter(paramObj.getRangeStart(), paramObj.getRangeEnd(), jpaQuery);
        jpaQuery.offset(paramObj.getOffset()).limit(paramObj.getSize());
        return jpaQuery.fetch();
    }

    private void applyCategoriesFilter(List<Long> categories, JPAQuery<Event> jpaQuery) {
        jpaQuery.where(event.category.id.in(categories));
    }

    private void applyDateRangeFilter(LocalDateTime rangeStart, LocalDateTime rangeEnd, JPAQuery<Event> jpaQuery) {
        if (rangeStart != null) {
            jpaQuery.where(event.eventDate.after(rangeStart));
        }
        if (rangeEnd != null) {
            jpaQuery.where(event.eventDate.before(rangeEnd));
        }
        if (rangeStart == null && rangeEnd == null) {
            jpaQuery.where(event.eventDate.after(LocalDateTime.now()));
        }
    }

    private void applyOrderByFilter(EventOrderBy orderBy, JPAQuery<Event> jpaQuery) {
        switch (orderBy) {
            case EVENT_DATE:
                jpaQuery.orderBy(event.eventDate.desc());
                break;
            case VIEWS:
                jpaQuery.orderBy(event.views.desc());
                break;
        }
    }

    private void applyOnlyAvailableFilter(Boolean onlyAvailable, JPAQuery<Event> jpaQuery) {
        if (onlyAvailable) {
            jpaQuery.where(event.participantLimit.eq(0).or(event.confirmedRequests.lt(event.participantLimit)));
        }
    }

    private void applyStatesFilter(List<EventState> states, JPAQuery<Event> jpaQuery) {
        jpaQuery.where(event.state.in(states));
    }

    private void applyPaidFilter(Boolean paid, JPAQuery<Event> jpaQuery) {
        jpaQuery.where(event.paid.eq(paid));
    }

    private void applyTextFilter(String text, JPAQuery<Event> jpaQuery) {
        jpaQuery.where(
                event.description.containsIgnoreCase(text)
                        .or(event.annotation.containsIgnoreCase(text))
        );
    }

    private void applyUsersFilter(List<Long> users, JPAQuery<Event> jpaQuery) {
        jpaQuery.where(event.initiator.id.in(users));
    }
}
