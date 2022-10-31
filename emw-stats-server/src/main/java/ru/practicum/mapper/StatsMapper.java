package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.controller.EndpointHit;
import ru.practicum.repository.StatisticEntity;

@Mapper
public interface StatsMapper {
    StatisticEntity dtoToEntity(EndpointHit dto);
}
