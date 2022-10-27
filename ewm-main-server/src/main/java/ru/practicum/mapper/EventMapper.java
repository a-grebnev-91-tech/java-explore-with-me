package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.event.*;
import ru.practicum.entity.Event;
import ru.practicum.model.UpdateEvent;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserReferenceMapper.class, CategoryMapper.class, CategoryReferenceMapper.class}
)
public interface EventMapper {
    List<EventFullDto> batchModelToFullDto(List<Event> entities);

    List<EventShortDto> batchModelToShortDto(List<Event> entities);

    @Mapping(source = "userId", target = "initiator")
    @Mapping(source = "event.category", target = "category")
    @Mapping(source = "event.location.lat", target = "lat")
    @Mapping(source = "event.location.lon", target = "lon")
    Event dtoToEntity(NewEventDto event, long userId);

    @Mapping(source = "lat", target = "location.lat")
    @Mapping(source = "lon", target = "location.lon")
    EventFullDto entityToFullDto(Event entity);

    @Mapping(source = "dto.category", target = "category")
    @Mapping(source = "dto.location.lat", target = "lat")
    @Mapping(source = "dto.location.lon", target = "lon")
    UpdateEvent updateDtoToModel(AdminUpdateEventRequest dto);

    @Mapping(source = "dto.category", target = "category")
    UpdateEvent updateDtoToModel(UpdateEventRequest dto);
}