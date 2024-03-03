package com.devapi.services;

import com.devapi.dao.*;
import com.devapi.entities.*;
import com.devapi.requestentities.CreateCardsRequest;
import com.devapi.requestentities.DeleteCardsRequest;
import com.devapi.requestentities.GetCardsRequest;
import com.devapi.requestentities.UpdateCardRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.devapi.util.DevApiUtilities.*;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    CardsRepository cardsRepository;

    @Autowired
    SubTopicRepository subTopicRepository;


    @Autowired
    TopicRepository topicRepository;

    @Autowired
    UserProgressDao userProgressDao;

    @Autowired
    private UserService userService;

    @Autowired
    private SubTopicProgressRepository subTopicProgressRepository;

    @Autowired
    private TopicProgressRepository topicProgressRepository;


    @Transactional
    public Card createCard(CreateCardsRequest createCardsRequest){
        UUID subTopicId = createCardsRequest.getSubTopicId();
        SubTopic subTopic = subTopicRepository.findById(subTopicId)
                .orElseThrow(() -> new EntityNotFoundException("SubTopic not found with ID: " + subTopicId));

        Card card = mapObjectToObjectWithModelMapper(createCardsRequest, Card.class);
        //System.out.println(card);
        card.setSubTopic(subTopic);
        // Save the SubTopic
        Card savedCard = cardsRepository.save(card);
        if (!subTopic.hasCard(savedCard)) {
            subTopic.setTotalCards(subTopic.getTotalCards() + 1);
        }
        return card;
    }

    @Transactional
    public void deleteCard(DeleteCardsRequest deleteCardsRequest){
        Card card = findCard(deleteCardsRequest.getCardId());
        cardsRepository.delete(card);
        //System.out.println(card);
        SubTopic subTopic = card.getSubTopic();
        subTopic.setTotalCards(subTopic.getTotalCards() - 1);
    }

    public Card findCard(UUID cardId){
        return cardsRepository.findById(cardId).orElseThrow(() -> new EntityNotFoundException("Card not found with ID: " + cardId));
    }

    @Transactional
    public void updateCardForUser(UpdateCardRequest updateCardRequest){
        UUID cardId = updateCardRequest.getCardId();
        Card card = findCard(updateCardRequest.getCardId());

        if (null != updateCardRequest.getUserId()) {
            User user = userService.findById(updateCardRequest.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + updateCardRequest.getUserId()));
            Timestamp currTimestamp = subtractDays(new Timestamp(System.currentTimeMillis()), 0, "+05:30");
            CardsProgress cardsProgress = new CardsProgress(new CardsProgress.CardsProgressId(user.getId(), cardId), currTimestamp, user, card);

            //found new Card for the user
            if (userProgressDao.findById(cardsProgress.getId()) == null) {

                SubTopicProgress subTopicProgress;
                Optional<SubTopicProgress> optionalSubTopicProgress = subTopicProgressRepository.findById(new SubTopicProgress.SubTopicProgressId(user.getId(), updateCardRequest.getSubTopicId()));
                if (optionalSubTopicProgress.isPresent()) {
                    subTopicProgress = optionalSubTopicProgress.get();
                    subTopicProgress.setCompletedCards(optionalSubTopicProgress.get().getCompletedCards() + 1);

                } else {
                    subTopicProgress = SubTopicProgress.builder().id(new SubTopicProgress.SubTopicProgressId(user.getId(), updateCardRequest.getSubTopicId())).CompletedCards(1).isCompleted(false).build();
                }

                Optional<SubTopic> subTopic = subTopicRepository.findById(updateCardRequest.getSubTopicId());
                if (subTopic.get().getTotalCards() == subTopicProgress.getCompletedCards()) {
                    //increment completed subtopics
                    subTopicProgress.setCompleted(true);
                    Optional<TopicProgress> optionalTopicProgress = topicProgressRepository.findById(new TopicProgress.TopicProgressId(user.getId(), updateCardRequest.getTopicId()));

                    TopicProgress topicProgress;
                    if (optionalTopicProgress.isPresent()) {
                        topicProgress = optionalTopicProgress.get();
                        topicProgress.setCompletedSubtopics(optionalTopicProgress.get().getCompletedSubtopics() + 1);
                    } else {
                        topicProgress = TopicProgress.builder().id(new TopicProgress.TopicProgressId(user.getId(), updateCardRequest.getTopicId())).completedSubtopics(1).isCompleted(false).build();
                    }


                    Optional<Topic> topic = topicRepository.findById(updateCardRequest.getTopicId());
                    if (topic.get().getNumberOfSubtopics() == topicProgress.getCompletedSubtopics()) {
                        //increment completed subtopics
                        topicProgress.setCompleted(true);
                    }
                    topicProgressRepository.save(topicProgress);
                }
                subTopicProgressRepository.save(subTopicProgress);

            }
            userProgressDao.save(cardsProgress);
        }
    }

    @Transactional
    public List<Card> getNCards(GetCardsRequest getCardsRequest) {
        UUID subTopicId = getCardsRequest.getSubTopicId();
        SubTopic subTopic = subTopicRepository.findById(subTopicId)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found with ID: " + subTopicId));
        Timestamp threeDaysAgoTimestamp = subtractDays(new Timestamp(System.currentTimeMillis()), 3, "+05:30");
        List<Card> cardsSeenNDaysAgo = cardsRepository.getCards(getCardsRequest.getUserId(), subTopic, threeDaysAgoTimestamp);
        List<Card> finalCards = new ArrayList<>();

        if (null != getCardsRequest.getUserId() && userService.findById(getCardsRequest.getUserId()).isPresent()) {
            List<Card> cardsNotSeen = userProgressDao.getCardsNotinProgress(getCardsRequest.getUserId(), getCardsRequest.getNoOfCards());

            System.out.println(cardsSeenNDaysAgo);
            System.out.println(cardsNotSeen);

            int totalCards = cardsSeenNDaysAgo.size() + cardsNotSeen.size();


            if (totalCards < getCardsRequest.getNoOfCards()) {
                List<Card> cardsSeenInNDays = cardsRepository.getCards1(getCardsRequest.getUserId(), subTopic, threeDaysAgoTimestamp);
                finalCards.addAll(cardsSeenNDaysAgo);
                finalCards.addAll(cardsNotSeen);
                if (totalCards + cardsSeenInNDays.size() < getCardsRequest.getNoOfCards())
                    throw new EntityNotFoundException("No. of Cards Requested more than their exist");
                for (int i = totalCards; i < getCardsRequest.getNoOfCards(); i++)
                    finalCards.add(cardsSeenInNDays.get(i - totalCards));
            } else {
                finalCards.addAll(cardsNotSeen);
                finalCards.addAll(cardsSeenNDaysAgo);
            }
        } else {
            finalCards.addAll(cardsRepository.getCardBySubTopicIs(subTopic));
        }
        //add ratio of the seen and not seen later on
        finalCards = getRandomSubset(finalCards, getCardsRequest.getNoOfCards());
        return finalCards;
    }
}
