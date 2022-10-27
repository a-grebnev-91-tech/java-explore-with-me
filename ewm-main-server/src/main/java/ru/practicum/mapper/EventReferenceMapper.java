package ru.practicum.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.entity.Event;
import ru.practicum.exception.NotFoundException;
import ru.practicum.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventReferenceMapper {
    private final EventRepository repo;

    public Event idToEvent(long id) {
        return repo.findById(id).orElseThrow(
                () -> new NotFoundException("Event not found", String.format("User with id %s isn't exist", id))
        );
    }

    public List<Event> batchIdsToEvents(List<Long> ids) {
        List<Event> events = repo.findAllById(ids);
        if (events.size() == ids.size()) {
            return events;
        } else {
            List<Long> found = events.stream().map(Event::getId).collect(Collectors.toList());
            ids.removeAll(found);
            throw new NotFoundException(
                    "Event(s) not found",
                    String.format("Event(s) with id(s) isn't exists %s", ids)
            );
        }
    }
}

