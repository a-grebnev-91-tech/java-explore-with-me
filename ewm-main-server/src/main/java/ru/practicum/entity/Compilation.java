package ru.practicum.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "pinned", nullable = false)
    private Boolean pinned;
    @ManyToMany
    @JoinTable(
            name = "events_compilations",
            joinColumns = { @JoinColumn(name = "compilation_id")},
            inverseJoinColumns = { @JoinColumn(name = "event_id")}
    )
    private Set<Event> events;
}