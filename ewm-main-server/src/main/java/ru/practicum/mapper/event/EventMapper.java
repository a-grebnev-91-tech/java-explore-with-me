package ru.practicum.mapper.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.entity.Event;
import ru.practicum.mapper.category.CategoryMapper;
import ru.practicum.mapper.category.CategoryReferenceMapper;
import ru.practicum.mapper.user.UserReferenceMapper;

@Mapper(componentModel = "spring",
        uses = {UserReferenceMapper.class, CategoryMapper.class, CategoryReferenceMapper.class}
)
public interface EventMapper {
    @Mapping(source = "userId", target = "initiator")
    @Mapping(source = "event.category", target = "category")
    @Mapping(source = "location.lat", target = "lat")
    @Mapping(source = "location.lon", target = "lon")
    Event dtoToEntity(NewEventDto event, long userId);

    @Mapping(source = "lat", target = "location.lat")
    @Mapping(source = "lon", target = "location.lon")
    EventFullDto modelToFullDto(Event entity);
}