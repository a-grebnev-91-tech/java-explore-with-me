package ru.practicum.repository;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import ru.practicum.entity.Event;
import ru.practicum.entity.QEvent;
import ru.practicum.model.EventState;
import ru.practicum.util.ParamObject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static ru.practicum.entity.QEvent.event;

@RequiredArgsConstructor
public class EventRepositoryCustomImpl implements EventRepositoryCustom {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Event> findAllByQueryDsl(ParamObject paramObj) {
        JPAQuery<Event> jpaQuery = new JPAQuery<>(entityManager);
        jpaQuery = jpaQuery.from(event).where(event.state.eq(EventState.PUBLISHED));
        if (paramObj.hasText()) {
            String text = paramObj.getText();
            jpaQuery = jpaQuery.where(
                    event.description.containsIgnoreCase(text)
                            .or(event.annotation.containsIgnoreCase(text))
            );
        }
        return jpaQuery.fetch();
    }
}
