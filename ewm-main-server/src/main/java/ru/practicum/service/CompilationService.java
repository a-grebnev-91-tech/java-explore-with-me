package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.entity.Compilation;
import ru.practicum.entity.Event;
import ru.practicum.exception.ForbiddenOperationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.util.OffsetPageable;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepo;
    private final EventRepository eventRepo;
    private final CompilationMapper mapper;

    public CompilationDto add(NewCompilationDto dto) {
        Compilation newComp = mapper.dtoToEntity(dto);
        newComp = compilationRepo.save(newComp);
        log.info("New compilation with id {} created", newComp.getId());
        return mapper.entityToDto(newComp);
    }

    public void addEventToCompilation(long compId, long eventId) {
        Event event = getEventOrThrow(eventId);
        Compilation comp = getCompilationOrThrow(compId);
        if (compAlreadyAdded(comp, eventId)) {
            throw new ForbiddenOperationException(
                    "Couldn't re-add event to compilation",
                    String.format("Event with ID %d already exist in compilation with ID %d", eventId, comp.getId())
            );
        }
        comp.getEvents().add(event);
        log.info("Event with ID {} added to compilation with ID {}", eventId, compId);
    }

    public void delete(long id) {
        Compilation comp = getCompilationOrThrow(id);
        compilationRepo.delete(comp);
    }

    public List<CompilationDto> findAll(Boolean pinned, int from, int size) {
        Pageable pageable = OffsetPageable.of(from, size);
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepo.findAll(pageable).getContent();
            log.info("Received all compilations");
        } else {
            compilations = compilationRepo.findAllByPinned(pinned, pageable);
            log.info("Received all compilations where pinned = {}", pinned);
        }
        return mapper.batchEntitiesToDtos(compilations);
    }

    public CompilationDto findById(long id) {
        Compilation comp = getCompilationOrThrow(id);
        log.info("Received compilation with ID {}", id);
        return mapper.entityToDto(comp);
    }

    public void pin(long compId) {
        Compilation comp = getCompilationOrThrow(compId);
        if (comp.getPinned()) {
            throw new ForbiddenOperationException(
                    "Compilation already pinned",
                    String.format("Compilation with ID %d already pinned", compId)
            );
        } else {
            comp.setPinned(true);
            log.info("Pinned compilation with ID {}", compId);
        }
    }

    public void removeEventFromCompilation(long compId, long eventId) {
        Event event = getEventOrThrow(eventId);
        Compilation comp = getCompilationOrThrow(compId);
        if (compAlreadyAdded(comp, eventId)) {
            comp.getEvents().removeIf(e -> e.getId() == eventId);
            log.info("Event with ID {} removed from compilation with ID {}", eventId, compId);
        } else {
            throw new ForbiddenOperationException(
                    "Couldn't remove event from compilation",
                    String.format("Event with ID %d isn't exist in compilation with ID %d", eventId, comp.getId())
            );
        }
    }

    public void unpin(long compId) {
        Compilation comp = getCompilationOrThrow(compId);
        if (comp.getPinned()) {
            comp.setPinned(false);
            log.info("Compilation with ID {} unpinned", compId);
        } else {
            throw new ForbiddenOperationException(
                    "Compilation already unpinned",
                    String.format("Compilation with ID %d already unpinned", compId)
            );
        }
    }

    private boolean compAlreadyAdded(Compilation comp, long eventId) {
        return comp.getEvents().stream().map(Event::getId).collect(Collectors.toList()).contains(eventId);
    }

    private Compilation getCompilationOrThrow(long compId) {
        return compilationRepo.findById(compId).orElseThrow(
                () -> new NotFoundException(
                        "Compilation not found",
                        String.format("Compilation with id %d isn't exist", compId)
                )
        );
    }

    private Event getEventOrThrow(long eventId) {
        return eventRepo.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event not found", String.format("Event with id %d isn't exist", eventId))
        );
    }
}
