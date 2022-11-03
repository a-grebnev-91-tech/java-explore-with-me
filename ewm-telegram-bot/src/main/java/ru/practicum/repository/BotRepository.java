package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.entity.TelegramUser;

import java.util.List;
import java.util.Optional;

public interface BotRepository extends JpaRepository<TelegramUser, Long> {
    Optional<TelegramUser> findByEwmId(long id);

    List<TelegramUser> findAllByNotifyEventPublished(boolean notifyEventPublished);
}
