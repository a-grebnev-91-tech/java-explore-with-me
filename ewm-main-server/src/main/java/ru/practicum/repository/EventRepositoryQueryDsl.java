package ru.practicum.repository;

import ru.practicum.entity.Event;
import ru.practicum.util.AdminEventParamObj;
import ru.practicum.util.PublicEventParamObj;

import java.util.List;

public interface EventRepositoryQueryDsl {
    List<Event> findAllByPublicParams(PublicEventParamObj paramObj);

    List<Event> findAllByAdminParams(AdminEventParamObj paramObj);
}
