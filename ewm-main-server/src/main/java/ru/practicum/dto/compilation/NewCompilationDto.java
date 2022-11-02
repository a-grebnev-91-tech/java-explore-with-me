package ru.practicum.dto.compilation;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class NewCompilationDto {
    private List<Long> events;
    private boolean pinned;
    @NotBlank
    private String title;
}
