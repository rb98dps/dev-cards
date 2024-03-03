package com.devapi.dao;


import com.devapi.entities.Card;
import com.devapi.entities.SubTopic;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface CardsRepository extends JpaRepository<Card, UUID> {

    public List<Card> getCardBySubTopicIs(@NonNull SubTopic subTopic);

    @Query("select c.id,c.code,c.description,c.title from Card c inner join CardsProgress p on c.id=p.id.cardId where " +
            "p.id.userId = ?1 and c.subTopic = ?2 and p.lastSeen < ?3 order by p.lastSeen")
    public List<Card> getCards(UUID userId, SubTopic subTopic, Timestamp timestamp);

    @Query("select c.id,c.code,c.description,c.title from Card c inner join CardsProgress p on c.id=p.id.cardId where " +
            "p.id.userId = ?1 and c.subTopic = ?2 and p.lastSeen >= ?3 order by p.lastSeen")
    public List<Card> getCards1(UUID userId, SubTopic subTopic, Timestamp timestamp);
}
