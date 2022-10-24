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
import ru.practicum.model.EventState;
import ru.practicum.model.RequestStatus;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepo;
    private final UserRepository userRepo;
    private final EventRepository eventRepo;
    private final RequestMapper mapper;

    public ParticipationRequestDto addRequest(long userId, long eventId) {
        Event event = getEventOrThrow(eventId);
        User user = getUserOrThrow(userId);
        checkIsNotInitiatorOrThrow(event, userId);
        checkIsPublishedOrThrow(event, eventId);
        checkIsFullyLoadOrThrow(event);
        checkRequestIsNotExistOrThrow(userId, eventId);
        ParticipationRequest request = createRequest(event, user);
        return mapper.entityToDto(requestRepo.save(request));
    }

    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        ParticipationRequest request = getRequestOrThrow(requestId);
        checkUserIsRequesterOrThrow(request, userId);
        Event event = request.getEvent();
        switch (request.getStatus()) {
            case CONFIRMED:
                long requestsCount = event.getConfirmedRequests();
                requestsCount--;
                event.setConfirmedRequests(requestsCount);
            case PENDING:
                request.setStatus(RequestStatus.CANCELED);
                break;
        }
        return mapper.entityToDto(request);
    }

    public ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId) {
        ParticipationRequest request = getRequestOrThrow(reqId);
        checkRequestIsUpdatableOrThrow(request, userId, eventId);
        return mapper.entityToDto(confirm(request));
    }

    public List<ParticipationRequestDto> findRequestsByEvent(long userId, long eventId) {
        Event event = getEventOrThrow(eventId);
        checkIsInitiatorOrThrow(event, userId);
        return mapper.batchEntitiesToDto(requestRepo.findAllByEventId(eventId));
    }

    public List<ParticipationRequestDto> findRequestsByUser(long userId) {
        checkUserExistingOrThrow(userId);
        return mapper.batchEntitiesToDto(requestRepo.findAllByRequesterId(userId));
    }

    public ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId) {
        ParticipationRequest request = getRequestOrThrow(reqId);
        checkRequestIsUpdatableOrThrow(request, userId, eventId);
        return mapper.entityToDto(reject(request));
    }

    private void checkEventExistingOrThrow(long eventId) {
        if (!eventRepo.existsById(eventId))
            throw new NotFoundException("Event not found", String.format("Event with id %d isn't exist", eventId));
    }

    private void checkIsFullyLoadOrThrow(Event event) {
        if (isEventFullyLoad(event)) {
            throw new ForbiddenOperationException(
                    "Event is fully load",
                    String.format(
                            "Event with id %d reached participation limit",
                            event.getId()
                    )
            );
        }
    }

    private void checkIsInitiatorOrThrow(Event event, long userId) {
        if (!isInitiator(event, userId)) {
            checkUserExistingOrThrow(userId);
            throw new ForbiddenOperationException(
                    "Only the initiator has access to this operation",
                    String.format("User with id %d isn't initiator of event with id %d", userId, event.getId())
            );
        }
    }

    private void checkIsNotInitiatorOrThrow(Event event, long userId) {
        if (isInitiator(event, userId)) {
            checkUserExistingOrThrow(userId);
            throw new ForbiddenOperationException(
                    "Initiator couldn't create a participation request for his event",
                    String.format("User with ID %d is the initiator of event with ID %d", userId, event.getId())
            );
        }
    }

    private void checkIsPublishedOrThrow(Event event, long eventId) {
        if (event.getState() != EventState.PUBLISHED) {
            throw new ForbiddenOperationException(
                    "Adding participation requests is only allowed for events in the PUBLISHED state",
                    String.format("Event with ID %d is in %s state", eventId, event.getState().toString())
            );
        }
    }

    private void checkRequestIsNotExistOrThrow(long userId, long eventId) {
        Optional<ParticipationRequest> existingRequest = requestRepo.findByEventIdAndRequesterId(eventId, userId);
        if (existingRequest.isPresent())
            throw new ForbiddenOperationException(
                    "Request already exist",
                    String.format("User with ID %d has already created request for event with ID %d", userId, eventId));
    }

    private void checkRequestIsUpdatableOrThrow(ParticipationRequest request, long userId, long eventId) {
        Event event = request.getEvent();
        if (event.getId() != eventId) {
            checkEventExistingOrThrow(eventId);
            throw new ValidationException(
                    "Request doesn't match event",
                    String.format("Request with id %d isn't for event with id %d", request.getId(), eventId)
            );
        }
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new ForbiddenOperationException(
                    "Request status should be PENDING",
                    String.format("Status of request with id %d is %s", request.getId(), request.getStatus().toString())
            );
        }
        checkIsInitiatorOrThrow(event, userId);
        checkIsFullyLoadOrThrow(event);
    }

    private void checkUserExistingOrThrow(long userId) {
        if (!userRepo.existsById(userId))
            throw new NotFoundException("User not found", String.format("User with id %d isn't exist", userId));
    }

    private void checkUserIsRequesterOrThrow(ParticipationRequest request, long userId) {
        if (request.getRequester().getId() != userId) {
            checkUserExistingOrThrow(userId);
            throw new ForbiddenOperationException(
                    "Access denied",
                    String.format("User with id %d hasn't access to request with id %d", userId, request.getId())
            );
        }
    }

    private ParticipationRequest confirm(ParticipationRequest request) {
        Event event = request.getEvent();
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        request.setStatus(RequestStatus.CONFIRMED);
        requestRepo.flush();
        if (isEventFullyLoad(event)) {
            requestRepo.rejectAllRequestByEventId(event.getId());
        }
        return request;
    }

    private ParticipationRequest createRequest(Event event, User user) {
        RequestStatus status;
        if (event.getRequestModeration()) {
            status = RequestStatus.PENDING;
        } else {
            status = RequestStatus.CONFIRMED;
        }
        return new ParticipationRequest(event, user, LocalDateTime.now(), status);
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

    private User getUserOrThrow(long userId) {
        return userRepo.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found", String.format("User with id %d isn't exist", userId)
                ));
    }

    private boolean isEventFullyLoad(Event event) {
        return event.getConfirmedRequests() >= event.getParticipantLimit();
    }

    private boolean isInitiator(Event event, long userId) {
        return event.getInitiator().getId() == userId;
    }

    private ParticipationRequest reject(ParticipationRequest request) {
        request.setStatus(RequestStatus.REJECTED);
        return request;
    }
}
