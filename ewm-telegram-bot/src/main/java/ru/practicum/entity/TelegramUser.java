package ru.practicum.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "users")
public class TelegramUser {
    @Id
    @Column(name = "telegram_id")
    private Long telegramId;
    @Column(name = "ewm_id")
    private Long ewmId;
    @Column(name = "notify_event_published")
    private Boolean notifyEventPublished;
}
