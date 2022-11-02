package ru.practicum.dto.category;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.validation.UniqueCategoryName;

@Getter
@Setter
public class NewCategoryDto {
    @UniqueCategoryName
    private String name;
}
