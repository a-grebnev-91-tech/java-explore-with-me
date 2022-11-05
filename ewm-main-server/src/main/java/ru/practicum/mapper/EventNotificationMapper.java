package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.EventNotification;
import ru.practicum.entity.Event;

@Mapper
public interface EventNotificationMapper {
    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.initiator.id", target = "initiatorId")
    @Mapping(source = "event.initiator.name", target = "initiatorName")
    EventNotification eventToNotification(Event event);
}