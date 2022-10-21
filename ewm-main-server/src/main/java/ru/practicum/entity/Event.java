package ru.practicum.entity;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.EventState;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "annotation", nullable = false)
    private String annotation;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @ManyToOne()
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventState state;
    @Column(name = "paid", nullable = false)
    private Boolean paid;
    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;
    @Column(name = "participant_limit")
    private int participantLimit;
    @Column(name = "confirmed_requests")
    private long confirmedRequests;
    @Column(name = "lat")
    private double lat;
    @Column(name = "lon")
    private double lon;
    @Column(name = "views")
    private long views;
}
