package ru.practicum.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

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
    @Column(name = "user_name")
    private String userName;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "registered_at")
    private LocalDateTime registeredAt = LocalDateTime.now();
    @Column(name = "notify_event_published", nullable = false)
    private Boolean notifyEventPublished;
    @Column(name = "notify_my_event", nullable = false)
    private Boolean notifyMyEvent;
    @Column(name = "notify_incoming_event", nullable = false)
    private Boolean notifyIncoming;
    @Column(name = "notify_my_participation", nullable = false)
    private Boolean participationMy;
    @Column(name = "notify_participation_request", nullable = false)
    private Boolean participationRequest;
}
