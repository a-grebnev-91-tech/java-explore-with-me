package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.validation.UniqueEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class NewUserRequest {
    @Email
    @UniqueEmail
    private String email;
    @NotBlank
    private String name;
}
