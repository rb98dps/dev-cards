package com.devapi.services;

import com.devapi.Exceptions.CustomDevApiException;
import com.devapi.entities.SubTopic;
import com.devapi.requestentities.AddFavouriteRequest;
import com.devapi.requestentities.CreateSubTopicRequest;

public interface SubTopicService {

    public SubTopic createSubTopic(CreateSubTopicRequest createSubTopicRequest) throws CustomDevApiException;

    public void addFavouriteToSubTopic(AddFavouriteRequest addFavouriteRequest) throws CustomDevApiException;
}
