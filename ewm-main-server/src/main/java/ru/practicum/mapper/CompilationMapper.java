package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.entity.Compilation;

@Mapper(componentModel = "spring", uses = EventReferenceMapper.class)
public interface CompilationMapper {
    Compilation dtoToEntity(NewCompilationDto dto);

    CompilationDto entityToDto(Compilation entity);
}
