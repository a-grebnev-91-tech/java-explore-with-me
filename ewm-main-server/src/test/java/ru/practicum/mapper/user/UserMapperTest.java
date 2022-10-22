package ru.practicum.mapper.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.entity.User;
import ru.practicum.mapper.UserMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    private static final long ID = 1L;
    private static final String NAME = "Test";
    private static final String EMAIL = "test@mail.ru";

    private static User entity;
    private static NewUserRequest inputDto;

    private static UserMapper mapper;

    @BeforeAll
    static void beforeAll() {
        entity = new User();
        entity.setId(ID);
        entity.setName(NAME);
        entity.setEmail(EMAIL);

        inputDto = new NewUserRequest();
        inputDto.setEmail(EMAIL);
        inputDto.setName(NAME);

        mapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void test1_convertInputDtoToEntity() {
        User user = mapper.dtoToEntity(inputDto);
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals(NAME, user.getName());
        assertEquals(EMAIL, user.getEmail());
    }

    @Test
    void test2_convertEntityToOutputDto() {
        UserDto dto = mapper.entityToDto(entity);
        assertNotNull(dto);
        assertEquals(ID, dto.getId());
        assertEquals(NAME, dto.getName());
        assertEquals(EMAIL, dto.getEmail());
    }

    @Test
    void test3_convertBatchEntitiesToOutputDtos() {
        List<UserDto> dtos = mapper.batchEntitiesToDto(List.of(entity));
        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertEquals(ID, dtos.get(0).getId());
        assertEquals(NAME, dtos.get(0).getName());
        assertEquals(EMAIL, dtos.get(0).getEmail());
    }
}