package com.devapi.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "subtopic_progress")
@NoArgsConstructor
@Builder
public class SubTopicProgress {

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("subTopicId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subtopic_id")
    private SubTopic subTopic;

    @Embeddable
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubTopicProgressId implements Serializable {
        @Column(name = "user_id")
        private UUID userId;
        @Column(name = "subtopic_id")
        private UUID subTopicId;


        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" +
                    "userId = " + userId + ", " +
                    "subTopicId = " + subTopicId + ")";
        }
    }

    @EmbeddedId
    private SubTopicProgressId id;

    int CompletedCards;

    boolean isCompleted;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "EmbeddedId = " + id + ", " +
                "CompletedCards = " + CompletedCards + ")";
    }
}
