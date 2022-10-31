package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.EndpointHit;
import ru.practicum.repository.StatisticEntity;

@Mapper
public interface StatsMapper {
    StatisticEntity dtoToEntity(EndpointHit dto);
}
