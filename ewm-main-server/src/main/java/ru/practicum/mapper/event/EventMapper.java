package ru.practicum.mapper.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.entity.Event;
import ru.practicum.mapper.category.CategoryReferenceMapper;
import ru.practicum.mapper.user.UserReferenceMapper;

@Mapper(componentModel = "spring", uses = {UserReferenceMapper.class, CategoryReferenceMapper.class})
public interface EventMapper {
    @Mapping(source = "userId", target = "initiator")
    @Mapping(source = "event.category", target = "category")
    Event dtoToEntity(NewEventDto event, long userId);
}
