package ru.practicum.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.entity.User;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.repository.UserRepository;
import ru.practicum.util.OffsetPageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class UserServiceTest {
    private static final String EMAIL = "test@test.ru";
    private static final String NAME = "Test";
    private static final long ID = 1L;

    private UserService service;
    @Mock
    private UserMapper mapper;
    @Mock
    private UserRepository repo;

    private User user;
    private NewUserRequest inputDto;
    private UserDto outputDto;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setEmail(EMAIL);
        user.setId(ID);
        user.setName(NAME);
        inputDto = new NewUserRequest();
        inputDto.setEmail(EMAIL);
        inputDto.setName(NAME);
        outputDto = new UserDto();
        outputDto.setEmail(EMAIL);
        outputDto.setId(ID);
        outputDto.setName(NAME);

        service = new UserService(repo, mapper);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(repo, mapper);
    }

    @Test
    void test1_shouldCreateUser() {
        when(mapper.dtoToEntity(inputDto)).thenReturn(user);
        when(mapper.entityToDto(user)).thenReturn(outputDto);
        when(repo.save(user)).thenReturn(user);

        UserDto fromServiceDto = service.add(inputDto);
        assertEquals(outputDto.getId(), fromServiceDto.getId());
        assertEquals(outputDto.getEmail(), fromServiceDto.getEmail());
        assertEquals(outputDto.getName(), fromServiceDto.getName());

        verify(mapper).dtoToEntity(inputDto);
        verify(mapper).entityToDto(user);
        verify(repo).save(user);
    }

    @Test
    void test2_shouldDeleteUser() {
        when(repo.findById(any())).thenReturn(Optional.empty());
        when(repo.findById(ID)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> service.delete(ID));
        verify(repo).findById(ID);
        verify(repo).delete(user);

        assertThrows(NotFoundException.class, () -> service.delete(999L));
        verify(repo).findById(999L);
    }

    @Test
    void test3_shouldReceivedBatchUsers() {
        OffsetPageable pageable = OffsetPageable.of(0, 1);
        when(repo.findAll(pageable)).thenReturn(new PageImpl<>(List.of(user)));
        when(repo.findAllByIdIn(List.of(ID), pageable)).thenReturn(new PageImpl<>(List.of(user)));
        when(mapper.batchEntitiesToDto(List.of(user))).thenReturn(List.of(outputDto));

        List<UserDto> outputDtos = service.findAll(null, 0, 1);
        assertNotNull(outputDtos);
        assertEquals(1, outputDtos.size());
        assertEquals(ID, outputDtos.get(0).getId());
        assertEquals(NAME, outputDtos.get(0).getName());
        assertEquals(EMAIL, outputDtos.get(0).getEmail());
        verify(repo).findAll(pageable);

        outputDtos = service.findAll(List.of(ID), 0, 1);
        assertNotNull(outputDtos);
        assertEquals(1, outputDtos.size());
        assertEquals(ID, outputDtos.get(0).getId());
        assertEquals(NAME, outputDtos.get(0).getName());
        assertEquals(EMAIL, outputDtos.get(0).getEmail());
        verify(repo).findAllByIdIn(List.of(ID), pageable);
        verify(mapper, times(2)).batchEntitiesToDto(List.of(user));
    }
}