package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.entity.Compilation;

import java.util.List;

@Mapper(componentModel = "spring", uses = EventReferenceMapper.class)
public interface CompilationMapper {
    List<CompilationDto> batchEntitiesToDtos(List<Compilation> compilations);

    Compilation dtoToEntity(NewCompilationDto dto);

    CompilationDto entityToDto(Compilation entity);
}
