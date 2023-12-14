package com.devapi.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_history")
@NoArgsConstructor
public class CardsProgress {

    @Embeddable
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CardsProgressId implements Serializable {
        @Column(name = "user_id")
        private UUID userId;
        @Column(name = "card_id")
        private UUID cardId;


        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" +
                    "userId = " + userId + ", " +
                    "cardId = " + cardId + ")";
        }
    }

    @EmbeddedId
    private CardsProgressId id;

    @Column(name= "last_seen")
    Timestamp lastSeen;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "card_id", insertable = false, updatable = false)
    private Card card;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "EmbeddedId = " + id + ", " +
                "CompletedCards = " + lastSeen + ")";
    }
}

