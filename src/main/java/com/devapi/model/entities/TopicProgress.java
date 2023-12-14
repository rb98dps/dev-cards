package com.devapi.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "topic_progress")
@NoArgsConstructor
public class TopicProgress {

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
