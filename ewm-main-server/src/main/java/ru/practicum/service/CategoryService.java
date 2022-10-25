package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.entity.Category;
import ru.practicum.exception.ForbiddenOperationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.util.OffsetPageable;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepo;
    private final EventRepository eventRepo;
    private final CategoryMapper mapper;

    public CategoryDto add(NewCategoryDto dto) {
        Category category = mapper.dtoToEntity(dto);
        category = categoryRepo.save(category);
        log.info("Category with name {} created", category.getName());
        return mapper.entityToDto(category);
    }

    public void delete(long catId) {
        if (eventRepo.existsByCategoryId(catId)) {
            throw new ForbiddenOperationException(
                    "Deleting a category is forbidden",
                    "Allowed to delete only categories without events"
            );
        } else {
            categoryRepo.deleteById(catId);
            log.info("Category with ID {} removed", catId);
        }
    }

    public List<CategoryDto> findAll(int from, int size) {
        Pageable pageable = OffsetPageable.of(from, size);
        return mapper.batchEntitiesToDto(categoryRepo.findAll(pageable).getContent());
    }

    public CategoryDto findById(long id) {
        Category category = getCategoryOrThrow(id);
        log.info("Getting category with ID {}", id);
        return mapper.entityToDto(category);
    }

    public CategoryDto update(CategoryDto dto) {
        Category existing = getCategoryOrThrow(dto.getId());
        existing.setName(dto.getName());
        log.info("Name for category with ID {} updated to {}", dto.getId(), dto.getName());
        return mapper.entityToDto(existing);
    }

    private Category getCategoryOrThrow(long id) {
        return categoryRepo.findById(id).orElseThrow(
                () -> new NotFoundException(
                        "Category not found",
                        String.format("Category with id %d isn't exist", id)
                )
        );
    }
}
