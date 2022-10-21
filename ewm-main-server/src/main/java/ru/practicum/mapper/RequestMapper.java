package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.entity.ParticipationRequest;

@Mapper
public interface RequestMapper {
    @Mapping(source = "entity.event.id", target = "event")
    @Mapping(source = "entity.requester.id", target = "requester")
    ParticipationRequestDto entityToDto(ParticipationRequest entity);
}
