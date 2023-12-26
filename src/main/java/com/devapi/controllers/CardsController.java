package com.devapi.controllers;

import com.devapi.dao.*;
import com.devapi.model.entities.*;
import com.devapi.model.requestentities.CreateCardsRequest;
import com.devapi.model.requestentities.DeleteCardsRequest;
import com.devapi.model.requestentities.GetCardsRequest;
import com.devapi.model.requestentities.UpdateCardRequest;
import com.devapi.responseObjects.BasicResponse;
import com.devapi.responseObjects.Response;
import com.devapi.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.devapi.util.DevApiUtilities.*;

@RepositoryRestController(path = "/cards")
public class CardsController {

    @Autowired
    CardsRepository cardsRepository;

    @Autowired
    SubTopicRepository subTopicRepository;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    UserProgressDao userProgressDao;
    private UserService userService;

    @Autowired
    private SubTopicProgressRepository subTopicProgressRepository;

    @Autowired
    private TopicProgressRepository topicProgressRepository;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-card")
    @Transactional
    public ResponseEntity<Response> createCards(@RequestBody @NonNull CreateCardsRequest createCardsRequest) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        UUID subTopicId = createCardsRequest.getSubTopicId();
        SubTopic subTopic = subTopicRepository.findById(subTopicId)
                .orElseThrow(() -> new EntityNotFoundException("SubTopic not found with ID: " + subTopicId));

        Card card = (Card) mapObjectToObject(createCardsRequest, Card.class);
        System.out.println(card);
        card.setSubTopic(subTopic);
        // Save the SubTopic
        Card savedCard = cardsRepository.save(card);
        if (!subTopic.hasCard(savedCard)) {
            subTopic.setTotalCards(subTopic.getTotalCards() + 1);
        }
        System.out.println(savedCard);
        return new ResponseEntity<>(new BasicResponse<>("Subtopic Created ", HttpStatus.OK.value(), savedCard), HttpStatus.OK);
    }

    @PostMapping("/delete-card")
    public ResponseEntity<Response> deleteCards(@RequestBody @NonNull DeleteCardsRequest deleteCardsRequest) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        Card card = cardsRepository.findById(deleteCardsRequest.getCardId()).orElseThrow(() -> new EntityNotFoundException("No card with given Id found"));
        cardsRepository.delete(card);
        System.out.println(card);
        SubTopic subTopic = card.getSubTopic();
        subTopic.setTotalCards(subTopic.getTotalCards() - 1);
        //delete from progress tables as well in future
        return new ResponseEntity<>(new BasicResponse<>("Card Deleted ", HttpStatus.OK.value(), card), HttpStatus.OK);
    }

    @PostMapping("/get-cards")
    @Transactional
    public ResponseEntity<Response> getNCards(@RequestBody @NonNull GetCardsRequest getCardsRequest) {
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
        return new ResponseEntity<>(new BasicResponse<>("Cards ", HttpStatus.OK.value(), finalCards), HttpStatus.OK);
    }

    @PostMapping("/update")
    @Transactional
    public ResponseEntity<Response> updateCardForUser(@RequestBody @NonNull UpdateCardRequest updateCardRequest) {
        UUID cardId = updateCardRequest.getCardId();
        Card card = cardsRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with ID: " + cardId));

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
        return new ResponseEntity<>(new BasicResponse<>("Updated Progress", HttpStatus.OK.value()), HttpStatus.OK);
    }


}
