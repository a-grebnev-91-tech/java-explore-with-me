package ru.practicum.user;

import org.mapstruct.Mapper;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;
import ru.practicum.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {
    User dtoToEntity(NewUserRequest userRequest);

    UserDto entityToDto(User entity);

    List<UserDto> batchEntitiesToDto(List<User> users);
}
