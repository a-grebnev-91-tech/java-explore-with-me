package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.entity.ParticipationRequest;

import java.util.List;

@Mapper
public interface RequestMapper {
    List<ParticipationRequestDto> batchEntitiesToDto(List<ParticipationRequest> entities);

    @Mapping(source = "entity.event.id", target = "event")
    @Mapping(source = "entity.requester.id", target = "requester")
    ParticipationRequestDto entityToDto(ParticipationRequest entity);
}
