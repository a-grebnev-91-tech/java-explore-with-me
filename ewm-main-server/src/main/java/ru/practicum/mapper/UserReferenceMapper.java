package ru.practicum.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.entity.User;
import ru.practicum.exception.NotFoundException;
import ru.practicum.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserReferenceMapper {
    private final UserRepository repo;

    public User idToUser(long id) {
        return repo.findById(id).orElseThrow(
                () -> new NotFoundException("User not found", String.format("User with id %s isn't exist", id))
        );
    }
}

