package ru.practicum.mapper.category;

import org.mapstruct.Mapper;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.entity.Category;

@Mapper
public interface CategoryMapper {
    CategoryDto entityToDto(Category entity);
}
