package com.devapi.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "cards")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(length = 100)
    private String title;

    @ManyToOne
    @JoinColumn(name = "subtopic_id", nullable = false)
    @NonNull
    private SubTopic subTopic;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @Column(columnDefinition = "LONGTEXT")
    private String code;

}
