package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stat")
@Builder(toBuilder = true)
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ToString.Exclude
    @JoinColumn(name = "app_id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER, targetEntity = App.class)
    private App app;

    @Column(name = "uri", nullable = false)
    private String uri;

    @Column(name = "ip", nullable = false)
    private String ip;

    @Column(name = "requested", nullable = false)
    private LocalDateTime requested;

}