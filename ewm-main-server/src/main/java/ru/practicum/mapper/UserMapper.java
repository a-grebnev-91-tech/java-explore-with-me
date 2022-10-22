package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {
    List<UserDto> batchEntitiesToDto(List<User> users);

    User dtoToEntity(NewUserRequest userRequest);

    UserDto entityToDto(User entity);

    UserShortDto entityToShortDto(User entity);
}
