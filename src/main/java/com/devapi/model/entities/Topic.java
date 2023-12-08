package com.devapi.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "topic")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Topic {

    @Id
    @Column(columnDefinition = "BINARY(16)",name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    @NonNull
    private UUID id;

    @Column(nullable = false, length = 100)
    @NonNull
    private String name;

    @Column(name = "no_of_users", nullable = false)
    @NonNull
    private int numberOfUsers;

    @Column(name = "no_of_subtopics", nullable = false)
    private int numberOfSubtopics;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return id.equals(topic.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @OneToMany(mappedBy = "topic",fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
    List<SubTopic> subTopicsList;

    public void addSubTopic(SubTopic subTopic){
        this.subTopicsList.add(subTopic);
    }
}
