package ru.practicum.repository;

import ru.practicum.entity.Event;
import ru.practicum.util.ParamObject;

import java.util.List;

public interface EventRepositoryQueryDsl {
    List<Event> findAllByQueryDsl(ParamObject paramObj);
}
