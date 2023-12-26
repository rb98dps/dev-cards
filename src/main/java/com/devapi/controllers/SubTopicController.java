package com.devapi.controllers;

import com.devapi.Exceptions.ExceptionResponse;
import com.devapi.dao.SubTopicRepository;
import com.devapi.dao.TopicRepository;
import com.devapi.model.entities.SubTopic;
import com.devapi.model.entities.Topic;
import com.devapi.model.entities.User;
import com.devapi.model.requestentities.AddFavouriteRequest;
import com.devapi.model.requestentities.CreateSubTopicRequest;
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
import java.util.Objects;
import java.util.UUID;

import static com.devapi.util.DevApiUtilities.mapObjectToObject;

@RepositoryRestController(path = "/subTopics")
public class SubTopicController {

    @Autowired
    SubTopicRepository subTopicRepository;

    @Autowired
    TopicRepository topicRepository;

    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-subtopic")
    public ResponseEntity<Response> createSubTopic(@RequestBody @NonNull CreateSubTopicRequest createSubTopicRequest) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        UUID topicId = createSubTopicRequest.getTopicId();
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found with ID: " + topicId));

        SubTopic subtopic = (SubTopic) mapObjectToObject(createSubTopicRequest,SubTopic.class);
        System.out.println(subtopic);
        // Set the retrieved Topic in the SubTopic
        subtopic.setTopic(topic);
        // Save the SubTopic
        SubTopic savedSubTopic = subTopicRepository.save(subtopic);
        System.out.println(savedSubTopic);
        topic.setNumberOfSubtopics(topic.getNumberOfSubtopics()+1);
        topicRepository.save(topic);
        return new ResponseEntity<>(new BasicResponse<>("Subtopic Created ", HttpStatus.OK.value(), savedSubTopic),HttpStatus.OK);
    }


    @PostMapping("/add-favourite")
    @Transactional
    public ResponseEntity<Response> addFavouriteToSubTopic(@RequestBody @NonNull AddFavouriteRequest addFavouriteRequest) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        UUID userId = addFavouriteRequest.getUserId();
        User user = userService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        UUID topicId = addFavouriteRequest.getTopicId();
        UUID subTopicId = addFavouriteRequest.getSubtopicId();
        if(Objects.nonNull(topicId)) {
            Topic topic = topicRepository.findById(topicId)
                    .orElseThrow(() -> new EntityNotFoundException("Topic not found with ID: " + topicId));
            user.addFavouriteTopic(topic);
        } else if (Objects.nonNull(subTopicId)) {
            SubTopic subTopic = subTopicRepository.findById(subTopicId)
                    .orElseThrow(() -> new EntityNotFoundException("Sub Topic not found with ID: " + subTopicId));
            user.addFavouriteSubTopic(subTopic);
        } else{
            new ResponseEntity<>(new ExceptionResponse<>("Missing required Attributes",HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new BasicResponse<>("Favourite Successfully Added ", HttpStatus.OK.value()),HttpStatus.OK);
    }
}
