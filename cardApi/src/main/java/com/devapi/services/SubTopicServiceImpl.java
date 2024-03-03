package com.devapi.services;

import com.devapi.Exceptions.CustomDevApiException;
import com.devapi.Exceptions.GeneralExceptions;
import com.devapi.dao.SubTopicRepository;
import com.devapi.dao.TopicRepository;
import com.devapi.entities.SubTopic;
import com.devapi.entities.Topic;
import com.devapi.entities.User;
import com.devapi.requestentities.AddFavouriteRequest;
import com.devapi.requestentities.CreateSubTopicRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

import static com.devapi.util.DevApiUtilities.mapObjectToObjectWithModelMapper;

@Service
public class SubTopicServiceImpl implements SubTopicService{

    @Autowired
    SubTopicRepository subTopicRepository;

    @Autowired
    TopicRepository topicRepository;

    UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Override
    @Transactional
    public SubTopic createSubTopic(CreateSubTopicRequest createSubTopicRequest) throws CustomDevApiException {
        UUID topicId = createSubTopicRequest.getTopicId();
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new CustomDevApiException("Topic not found with ID: " + topicId));

        SubTopic subtopic = mapObjectToObjectWithModelMapper(createSubTopicRequest,SubTopic.class);
        // Set the retrieved Topic in the SubTopic
        subtopic.setTopic(topic);
        // Save the SubTopic
        SubTopic savedSubTopic = subTopicRepository.save(subtopic);
        topic.setNumberOfSubtopics(topic.getNumberOfSubtopics()+1);
        topicRepository.save(topic);
        return savedSubTopic;
    }

    @Override
    public void addFavouriteToSubTopic(AddFavouriteRequest addFavouriteRequest) throws CustomDevApiException {
        UUID userId = addFavouriteRequest.getUserId();
        User user = userService.findById(userId)
                .orElseThrow(() -> new CustomDevApiException("User not found with ID: " + userId));

        UUID topicId = addFavouriteRequest.getTopicId();
        UUID subTopicId = addFavouriteRequest.getSubtopicId();
        if(Objects.nonNull(topicId)) {
            Topic topic = topicRepository.findById(topicId)
                    .orElseThrow(() -> new CustomDevApiException("Topic not found with ID: " + topicId));
            user.addFavouriteTopic(topic);
        } else if (Objects.nonNull(subTopicId)) {
            SubTopic subTopic = subTopicRepository.findById(subTopicId)
                    .orElseThrow(() -> new CustomDevApiException("Sub Topic not found with ID: " + subTopicId));
            user.addFavouriteSubTopic(subTopic);
        } else{
            throw new CustomDevApiException(GeneralExceptions.BadRequestException);
        }
    }
}
