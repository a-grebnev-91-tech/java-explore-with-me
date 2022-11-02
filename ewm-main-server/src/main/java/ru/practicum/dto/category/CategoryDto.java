package ru.practicum.dto.category;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.validation.UniqueCategoryName;

@Getter
@Setter
public class CategoryDto {
    private long id;
    @UniqueCategoryName
    private String name;
}
