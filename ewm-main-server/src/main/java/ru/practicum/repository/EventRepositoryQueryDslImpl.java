package ru.practicum.repository;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import ru.practicum.entity.Event;
import ru.practicum.model.EventState;
import ru.practicum.util.ParamObject;

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
    public List<Event> findAllByQueryDsl(ParamObject paramObj) {
        JPAQuery<Event> jpaQuery = new JPAQuery<>(entityManager);
        jpaQuery = jpaQuery.from(event).where(event.state.eq(EventState.PUBLISHED));
        applyTextFilter(paramObj, jpaQuery);
        applyCategoriesFilter(paramObj, jpaQuery);
        applyPaidFilter(paramObj, jpaQuery);
        applyDateRangeFilter(paramObj, jpaQuery);
        applyOnlyAvailableFilter(paramObj, jpaQuery);
        applyOrderByFilter(paramObj, jpaQuery);
        jpaQuery.offset(paramObj.getOffset()).limit(paramObj.getSize());
        return jpaQuery.fetch();
    }

    private void applyCategoriesFilter(ParamObject paramObj, JPAQuery<Event> jpaQuery) {
        if (paramObj.hasCategories()) {
            jpaQuery.where(event.category.id.in(paramObj.getCategories()));
        }
    }

    private void applyDateRangeFilter(ParamObject paramObj, JPAQuery<Event> jpaQuery) {
        if (paramObj.hasRangeStart()) {
            jpaQuery.where(event.eventDate.after(paramObj.getRangeStart()));
        }
        if (paramObj.hasRangeEnd()) {
            jpaQuery.where(event.eventDate.before(paramObj.getRangeEnd()));
        }
        if (!paramObj.hasRangeStart() && !paramObj.hasRangeEnd()) {
            jpaQuery.where(event.eventDate.after(LocalDateTime.now()));
        }
    }

    private void applyOrderByFilter(ParamObject paramObj, JPAQuery<Event> jpaQuery) {
        if (paramObj.hasOrderBy()) {
            switch (paramObj.getOrderBy()) {
                case EVENT_DATE:
                    jpaQuery.orderBy(event.eventDate.desc());
                    break;
                case VIEWS:
                    jpaQuery.orderBy(event.views.desc());
                    break;
            }
        }
    }

    private void applyOnlyAvailableFilter(ParamObject paramObj, JPAQuery<Event> jpaQuery) {
        if (paramObj.hasOnlyAvailable()) {
            jpaQuery.where(event.participantLimit.eq(0).or(event.confirmedRequests.lt(event.participantLimit)));
        }
    }

    private void applyPaidFilter(ParamObject paramObj, JPAQuery<Event> jpaQuery) {
        if (paramObj.hasPaid()) {
            jpaQuery.where(event.paid.eq(paramObj.getPaid()));
        }
    }

    private void applyTextFilter(ParamObject paramObj, JPAQuery<Event> jpaQuery) {
        if (paramObj.hasText()) {
            String text = paramObj.getText();
            jpaQuery.where(
                    event.description.containsIgnoreCase(text)
                            .or(event.annotation.containsIgnoreCase(text))
            );
        }
    }
}
