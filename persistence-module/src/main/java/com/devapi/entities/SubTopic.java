package com.devapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "subtopic")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class SubTopic {
    @Id
    @Column(columnDefinition = "BINARY(16)", name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "topic_id")
    @ToString.Exclude
    @JsonIgnore
    @NonNull
    private Topic topic;

    @Column(nullable = false, length = 100)
    @NonNull
    private String name;

    @Column(nullable = false, length = 100)
    private String description;

    @Column(name = "total_cards", nullable = false)
    private int totalCards;

    @OneToMany(mappedBy = "subTopic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Card> cards;

    @ManyToMany(mappedBy = "favouriteSubTopics")
    private Set<User> users = new LinkedHashSet<>();

    @OneToMany(mappedBy = "subTopic")
    private Set<Problem> problems = new LinkedHashSet<>();

    @OneToMany(mappedBy = "subTopic")
    private Set<SubTopicProgress> subtopicProgresses = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTopic subTopic = (SubTopic) o;
        return id.equals(subTopic.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public boolean hasCard(Card card){
        return this.cards.contains(card);
    }

}
