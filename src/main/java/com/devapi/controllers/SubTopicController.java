package com.devapi.controllers;

import com.devapi.dao.SubTopicRepository;
import com.devapi.dao.TopicRepository;
import com.devapi.model.entities.SubTopic;
import com.devapi.model.entities.Topic;
import com.devapi.model.requestentities.CreateSubTopicRequest;
import com.devapi.responseObjects.BasicResponse;
import com.devapi.responseObjects.Response;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static com.devapi.util.DevApiUtilities.mapObjectToObject;

@RepositoryRestController(path = "/subTopics")
public class SubTopicController {

    @Autowired
    SubTopicRepository subTopicRepository;

    @Autowired
    TopicRepository topicRepository;

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
}
