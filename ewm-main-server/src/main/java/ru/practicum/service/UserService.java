package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.entity.User;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.user.UserMapper;
import ru.practicum.repository.UserRepository;
import ru.practicum.util.OffsetPageable;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;
    private final UserMapper mapper;

    public UserDto add(NewUserRequest dto) {
        User user = mapper.dtoToEntity(dto);
        user = repo.save(user);
        log.info("User {} saved", user);
        return mapper.entityToDto(user);
    }

    public void delete(long userId) {
        User user = getExistingOrThrow(userId);
        repo.delete(user);
    }

    public List<UserDto> findAll(List<Long> ids, int from, int size) {
        OffsetPageable pageable = OffsetPageable.of(from, size);
        Page<User> users;
        if (ids == null) {
            users = repo.findAll(pageable);
            log.info("Received all users");
        } else {
            users = repo.findAllByIdIn(ids, pageable);
            log.info("Received users with id {}", ids);
        }
        return mapper.batchEntitiesToDto(users.getContent());
    }

    private User getExistingOrThrow(long userId) {
        return repo.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User with id %d isn't exist", userId)
                ));
    }
}
