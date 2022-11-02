package ru.practicum.controller.adm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.service.CategoryService;
import ru.practicum.validation.ExistingCategory;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryService service;

    @PostMapping
    public CategoryDto addCategory(@RequestBody @Valid NewCategoryDto dto) {
        log.info("Creating new category with name {}", dto.getName());
        return service.add(dto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable @ExistingCategory long catId) {
        log.info("Attempt to remove category with ID {}", catId);
        service.delete(catId);
    }

    @PatchMapping
    public CategoryDto updateCategory(@RequestBody @Valid CategoryDto dto) {
        log.info("Attempt to update category with ID {}", dto.getId());
        return service.update(dto);
    }
}
