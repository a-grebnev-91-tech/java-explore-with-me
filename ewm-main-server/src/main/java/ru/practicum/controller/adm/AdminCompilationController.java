package ru.practicum.controller.adm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private final CompilationService service;

    @PostMapping
    public CompilationDto addCompilation(@RequestBody @Valid NewCompilationDto dto) {
        log.info("Admin attempt to add new compilation with name {}", dto.getTitle());
        return service.add(dto);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEvent(@PathVariable @Positive long compId, @PathVariable @Positive long eventId) {
        log.info("Admin attempt to add event with ID {} to compilation with ID {}", eventId, compId);
        service.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable("compId") @Positive long id) {
        log.info("Admin attempt to delete compilation with ID {}", id);
        service.delete(id);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable("compId") @Positive long compId) {
        log.info("Admin attempt to pin compilation with ID {}", compId);
        service.pin(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void removeEvent(@PathVariable @Positive long compId, @PathVariable @Positive long eventId) {
        log.info("Admin attempt to remove event with ID {} from compilation with ID {}", eventId, compId);
        service.removeEventFromCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable("compId") @Positive long compId) {
        log.info("Admin attempt to unpin compilation with ID {}", compId);
        service.unpin(compId);
    }
}
