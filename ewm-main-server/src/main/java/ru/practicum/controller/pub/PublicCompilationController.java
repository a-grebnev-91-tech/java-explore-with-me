package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.service.CompilationService;

import javax.validation.constraints.Positive;
import java.util.List;

import static ru.practicum.util.Constants.DEFAULT_FROM_VALUE;
import static ru.practicum.util.Constants.DEFAULT_SIZE_VALUE;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class PublicCompilationController {
    private final CompilationService service;

    @GetMapping
    public List<CompilationDto> findAll(
            @RequestParam(value = "pinned", required = false) Boolean pinned,
            @RequestParam(value = "from", defaultValue = DEFAULT_FROM_VALUE) int from,
            @RequestParam(value = "size", defaultValue = DEFAULT_SIZE_VALUE) int size
    ) {
        log.info("Received all compilations");
        return service.findAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto findById(@PathVariable("compId") @Positive long id) {
        log.info("Attempt to receive compilation with ID {}", id);
        return service.findById(id);
    }
}
