package ru.practicum.mapper.user;

import org.mapstruct.Mapper;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {
    User dtoToEntity(NewUserRequest userRequest);

    UserDto entityToDto(User entity);

    List<UserDto> batchEntitiesToDto(List<User> users);
}
