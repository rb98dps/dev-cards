package com.devapi.dao;

import com.devapi.entities.Card;
import com.devapi.entities.CardsProgress;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UserProgressDao extends Dao<CardsProgress, CardsProgress.CardsProgressId> {

    UserProgressDao(){
        super();
        this.setClazz(CardsProgress.class);
    }


    public List<Card> getCardsNotinProgress(UUID userId, int numberOfCards){
        String jpql = "SELECT c FROM Card c " +
                "WHERE c NOT IN " +
                "(SELECT up.card FROM CardsProgress up WHERE up.id.userId = :userId) " +
                "ORDER BY c.id ASC";
        TypedQuery<Card> query = entityManager.createQuery(jpql, Card.class)
                .setParameter("userId", userId)
                .setMaxResults(numberOfCards);

        return query.getResultList();
    }
}
