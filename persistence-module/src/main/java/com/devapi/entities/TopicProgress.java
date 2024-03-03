package com.devapi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "topic_progress")
@NoArgsConstructor
@Builder
public class TopicProgress {

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Embeddable
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TopicProgressId implements Serializable {
        @Column(name = "user_id")
        private UUID userId;
        @Column(name = "topic_id")
        private UUID topicId;


        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" +
                    "userId = " + userId + ", " +
                    "TopicId = " + topicId + ")";
        }
    }

    @EmbeddedId
    private TopicProgressId id;
    @Column(name = "completed_subtopics")
    int completedSubtopics;

    boolean isCompleted;
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "EmbeddedId = " + id + ", " +
                "completedSubtopics = " + completedSubtopics + ")";
    }
}
