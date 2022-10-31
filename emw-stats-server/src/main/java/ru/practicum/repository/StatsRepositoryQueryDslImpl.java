package ru.practicum.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import ru.practicum.dto.ViewStats;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.repository.QStatisticEntity.statisticEntity;

//todo rm
@RequiredArgsConstructor
public class StatsRepositoryQueryDslImpl implements StatsRepositoryQueryDsl {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        query.from(statisticEntity)
                .where(statisticEntity.timestamp.after(start).and(statisticEntity.timestamp.before(end)));
        if (uris != null && !uris.isEmpty()) {
        }
//        JPAQuery<ViewStats> jpaQuery = new JPAQuery<>(entityManager);
//        jpaQuery.from(statisticEntity)
//                .where(statisticEntity.timestamp.after(start).and(statisticEntity.timestamp.before(end)));
//        if (uris != null && !uris.isEmpty()) {
//            jpaQuery.where(statisticEntity.uri.in(uris));
//        }
//        jpaQuery.groupBy(statisticEntity.uri).
//        if ()
        return null;
    }
}
