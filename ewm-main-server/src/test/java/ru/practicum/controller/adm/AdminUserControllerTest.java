package ru.practicum.controller.adm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.entity.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = AdminUserController.class)
class AdminUserControllerTest {
    private static final String PATH = "/admin/users";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService service;
    @MockBean
    private UserRepository repository;

    @Test
    void test1_shouldAddUser() throws Exception {
        UserDto outputDto = getDtos(1).get(0);

        NewUserRequest inputDto = new NewUserRequest();
        inputDto.setEmail(outputDto.getEmail());
        inputDto.setName(outputDto.getName());

        User user = new User();
        user.setId(outputDto.getId());
        user.setName(outputDto.getName());
        user.setEmail(outputDto.getEmail());

        when(service.add(inputDto)).thenReturn(outputDto);
        when(repository.findByEmail(inputDto.getEmail())).thenReturn(Optional.empty());

        mvc.perform(post(PATH)
                        .content(objectMapper.writeValueAsString(inputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(outputDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(outputDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(outputDto.getEmail()), String.class));
    }

    @Test
    void test2_shouldNotCreateUserWithInvalidEmail() throws Exception {
        NewUserRequest inputDto = new NewUserRequest();
        inputDto.setEmail("mail");
        inputDto.setName("name");

        mvc.perform(post(PATH)
                        .content(objectMapper.writeValueAsString(inputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("BAD_REQUEST"), String.class))
                .andExpect(jsonPath("$.reason", is("Validation failed"), String.class))
                .andExpect(jsonPath("$.message", startsWith("Validation failed for argument"), String.class));
    }

    private List<UserDto> getDtos(int count) {
        List<UserDto> dtos = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            UserDto dto = new UserDto();
            dto.setId(i + 1L);
            dto.setEmail("mail" + i + 1 + "@mail.ru");
            dto.setName("user" + i + 1);
            dtos.add(dto);
        }
        return dtos;
    }
}