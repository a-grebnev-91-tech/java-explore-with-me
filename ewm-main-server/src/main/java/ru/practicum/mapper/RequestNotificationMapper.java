package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.RequestNotification;
import ru.practicum.entity.ParticipationRequest;

@Mapper
public interface RequestNotificationMapper {
    @Mapping(source = "request.id", target = "requestId")
    @Mapping(source = "request.event.id", target = "eventId")
    @Mapping(source = "request.event.title", target = "eventTitle")
    @Mapping(source = "request.event.annotation", target = "eventAnnotation")
    @Mapping(source = "request.requester.id", target = "requesterId")
    @Mapping(source = "request.event.initiator.id", target = "eventInitiatorId")
    RequestNotification requestToNotification(ParticipationRequest request);
}
