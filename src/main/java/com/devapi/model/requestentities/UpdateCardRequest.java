package com.devapi.model.requestentities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@AllArgsConstructor
@Getter
@Setter
public class UpdateCardRequest {
    @JsonProperty("user_id")
    UUID userId;
    @JsonProperty("card_id")
    UUID cardId;
    @JsonProperty("subTopic_id")
    UUID SubTopicId;
    @JsonProperty("topic_id")
    UUID TopicId;
}
