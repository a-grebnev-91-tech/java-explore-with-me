package ru.practicum.repository;

import com.querydsl.jpa.impl.JPAQuery;
import ru.practicum.entity.Event;
import ru.practicum.entity.QEvent;
import ru.practicum.util.ParamObject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class EventRepositoryCustomImpl implements EventRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    public static QEvent qEvent;

    @Override
    public List<Event> findAllByQueryDsl(ParamObject paramObj) {
        JPAQuery<Event> jpaQuery = null;
        return null;
    }
}
