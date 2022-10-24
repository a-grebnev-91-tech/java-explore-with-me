package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.entity.Event;
import ru.practicum.entity.ParticipationRequest;
import ru.practicum.entity.User;
import ru.practicum.exception.ForbiddenOperationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.RequestStatus;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.util.List;

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

    public List<ParticipationRequestDto> findParticipationRequests(long userId, long eventId) {
        Event event = getEventOrThrow(eventId);
        checkInitiatorOrThrow(event, userId);
        return mapper.batchEntitiesToDto(requestRepo.findAllByEventId(eventId));
    }

    public ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId) {
        ParticipationRequest request = getRequestOrThrow(reqId);
        throwIfRequestCouldNotBeUpdateByInitiator(request, userId, eventId);
        return mapper.entityToDto(reject(request));
    }

    private void checkEventExistingOrThrow(long eventId) {
        if (!eventRepo.existsById(eventId))
            throw new NotFoundException("Event not found", String.format("Event with id %d isn't exist", eventId));
    }

    private void checkInitiatorOrThrow(Event event, long userId) {
        if (event.getInitiator().getId() != userId) {
            checkUserExistingOrThrow(userId);
            throw new ForbiddenOperationException(
                    "Only the initiator has access to this operation",
                    String.format("User with id %d isn't initiator of event with id %d", userId, event.getId())
            );
        }
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

    private Event getEventOrThrow(long eventId) {
        return eventRepo.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event not found", String.format("Event with id %d isn't exist", eventId))
        );
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
        if (event.getId() != eventId) {
            checkEventExistingOrThrow(eventId);
            throw new ValidationException(
                    "Request and event don't match",
                    String.format("Request with id %d isn't for event with id %d", request.getId(), eventId)
            );
        }
        checkInitiatorOrThrow(event, userId);
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
