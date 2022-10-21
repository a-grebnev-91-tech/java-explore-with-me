package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.entity.Event;
import ru.practicum.entity.ParticipationRequest;
import ru.practicum.entity.User;
import ru.practicum.exception.ForbiddenOperationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.RequestStatus;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepo;
    private final UserRepository userRepo;
    private final EventRepository eventRepo;
    private final RequestMapper mapper;

    public ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId) {
        ParticipationRequest request = getRequestOrThrow(reqId);
        throwIfRequestCouldNotBeUpdateByInitiator(request, userId, eventId);
        return mapper.entityToDto(confirm(request));
    }

    public ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId) {
        ParticipationRequest request = getRequestOrThrow(reqId);
        throwIfRequestCouldNotBeUpdateByInitiator(request,userId, eventId);
        return mapper.entityToDto(reject(request));
    }

    private void checkEventExistingOrThrow(long eventId) {
        if (!eventRepo.existsById(eventId))
            throw new NotFoundException("Event not found", String.format("Event with id %d isn't exist", eventId));
    }

    private void checkUserExistingOrThrow(long userId) {
        if (!userRepo.existsById(userId))
            throw new NotFoundException("User not found", String.format("User with id %d isn't exist", userId));
    }

    private ParticipationRequest confirm(ParticipationRequest request) {
        Event event = request.getEvent();
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        request.setStatus(RequestStatus.CONFIRMED);
        requestRepo.save(request);
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            eventRepo.rejectAllRequestByEventId(event.getId());
        }
        return request;
    }


    private ParticipationRequest getRequestOrThrow(long reqId) {
        return requestRepo.findById(reqId).orElseThrow(
                () -> new NotFoundException("Request not found", String.format("Request with id %d isn't exist", reqId))
        );
    }

    private ParticipationRequest reject(ParticipationRequest request) {
        request.setStatus(RequestStatus.REJECTED);
        return request;
    }

    private void throwIfRequestCouldNotBeUpdateByInitiator(ParticipationRequest request, long userId, long eventId) {
        Event event = request.getEvent();
        User initiator = event.getInitiator();
        if (event.getId() != eventId || initiator.getId() != userId) {
            checkUserExistingOrThrow(userId);
            checkEventExistingOrThrow(eventId);
            throw new ForbiddenOperationException(
                    "User doesn't have permission to confirm request",
                    String.format("User with id %d is not an initiator of event with id %d", userId, eventId)
            );
        }
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new ForbiddenOperationException(
                    "Request status should be PENDING",
                    String.format("Status of request with id %d is %s", request.getId(), request.getStatus().toString())
            );
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ForbiddenOperationException(
                    "Event is fully load",
                    String.format(
                            "Could not confirm request with id %d because event with id %d reached participation limit",
                            request.getId(),
                            eventId
                    )
            );
        }
    }
}
