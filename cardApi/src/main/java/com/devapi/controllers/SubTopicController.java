package com.devapi.controllers;

import com.devapi.Exceptions.CustomDevApiException;
import com.devapi.services.SubTopicService;
import com.devapi.entities.SubTopic;
import com.devapi.requestentities.AddFavouriteRequest;
import com.devapi.requestentities.CreateSubTopicRequest;
import com.devapi.responseObjects.BasicResponse;
import com.devapi.responseObjects.Response;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RepositoryRestController(path = "/subTopics")
public class SubTopicController {

    @Autowired
    SubTopicService subTopicService;


    @PostMapping("/create-subtopic")
    public Response createSubTopic(@RequestBody @NonNull CreateSubTopicRequest createSubTopicRequest) throws CustomDevApiException {
        SubTopic subTopic = subTopicService.createSubTopic(createSubTopicRequest);
        return new BasicResponse<>("Subtopic Created ", HttpStatus.OK.value(), subTopic);
    }


    @PostMapping("/add-favourite")
    @Transactional
    public ResponseEntity<Response> addFavouriteToSubTopic(@RequestBody @NonNull AddFavouriteRequest addFavouriteRequest) throws CustomDevApiException {
        subTopicService.addFavouriteToSubTopic(addFavouriteRequest);
        return new ResponseEntity<>(new BasicResponse<>("Favourite Successfully Added ", HttpStatus.OK.value()),HttpStatus.OK);
    }
}
