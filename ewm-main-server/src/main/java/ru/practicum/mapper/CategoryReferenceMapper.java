package ru.practicum.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.entity.Category;
import ru.practicum.exception.NotFoundException;
import ru.practicum.repository.CategoryRepository;

@Component
@RequiredArgsConstructor
public class CategoryReferenceMapper {
    private final CategoryRepository repo;

    public Category idToCategory(long id) {
        return repo.findById(id).orElseThrow(
                () -> new NotFoundException("Category not found", String.format("Category with id %s isn't exist", id))
        );
    }
}
