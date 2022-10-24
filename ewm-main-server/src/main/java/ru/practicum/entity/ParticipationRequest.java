package ru.practicum.entity;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "participation_requests")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    @Column(name = "created")
    private LocalDateTime created;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    public ParticipationRequest() {
    }

    public ParticipationRequest(Event event, User requester, LocalDateTime created, RequestStatus status) {
        this.event = event;
        this.requester = requester;
        this.created = created;
        this.status = status;
    }
}