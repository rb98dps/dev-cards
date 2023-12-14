package com.devapi.model.requestentities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@AllArgsConstructor
@Getter
@Setter
public class GetCardsRequest {
    @JsonProperty("user_id")
    UUID userId;
    @JsonProperty("subTopic_id")
    UUID subTopicId;
    @JsonProperty("topic_id")
    UUID TopicId;
    @JsonProperty("no_of_cards")
    int noOfCards;

}
