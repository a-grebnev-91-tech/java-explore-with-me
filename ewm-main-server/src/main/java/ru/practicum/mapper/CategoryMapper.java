package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.entity.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {
    Category dtoToEntity(NewCategoryDto dto);

    Category dtoToEntity(CategoryDto dto);

    CategoryDto entityToDto(Category entity);

    List<CategoryDto> batchEntitiesToDto(List<Category> entities);
}
