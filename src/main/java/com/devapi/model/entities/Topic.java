package com.devapi.model.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    @NonNull
    @JsonProperty("id")
    private UUID id;

    @Column(name = "topic_name",nullable = false, length = 100)
    @NonNull
    private String name;

    @Column(name = "no_of_users", nullable = false)
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
        if(null==subTopicsList)
            subTopicsList = new ArrayList<>();
        this.subTopicsList.add(subTopic);
    }
}
