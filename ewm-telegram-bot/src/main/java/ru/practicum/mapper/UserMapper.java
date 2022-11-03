package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.practicum.entity.TelegramUser;

@Mapper
public interface UserMapper {
    @Mapping(source = "id", target = "telegramId")
    TelegramUser mapToUser(User user);
}
