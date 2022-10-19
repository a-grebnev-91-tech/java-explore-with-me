package ru.practicum.controller.adm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService service;

    @PostMapping
    public UserDto add(@RequestBody @Valid NewUserRequest user) {
        log.info("Attempt to create new user {}", user);
        return service.add(user);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId") @Positive long userId) {
        log.info("Attempt to delete user with id {}", userId);
        service.delete(userId);
    }

    @GetMapping
    public List<UserDto> findAll(
            @RequestParam(value = "ids", required = false) List<Long> ids,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = "10") @Positive int size
    ) {
        if (ids == null) {
            log.info("Attempt to get all users");
        } else {
            log.info("Attempt to get users with id {}", ids);
        }
        return service.findAll(ids, from, size);
    }
}
