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
@Table(name = "subtopic_progress")
@NoArgsConstructor
public class SubTopicProgress {

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
