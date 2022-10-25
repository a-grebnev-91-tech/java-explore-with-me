package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.util.Constants.DEFAULT_FROM_VALUE;
import static ru.practicum.util.Constants.DEFAULT_SIZE_VALUE;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class PublicCategoryController {
    private final CategoryService service;

    @GetMapping
    public List<CategoryDto> findAll(
            @RequestParam(value = "from", defaultValue = DEFAULT_FROM_VALUE) @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = DEFAULT_SIZE_VALUE) @Positive int size
    ) {
        log.info("Getting all categories");
        return service.findAll(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto findById(@PathVariable("catId") @Positive long id) {
        log.info("Attempt to get category with ID {}", id);
        return service.findById(id);
    }
}
