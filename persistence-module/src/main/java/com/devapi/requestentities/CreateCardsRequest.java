package com.devapi.requestentities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class CreateCardsRequest {

    @JsonProperty("subTopic_id")
    UUID subTopicId;

    @JsonProperty("subTopic_name")
    String subTopicName;

    @JsonProperty("card_name")
    String title;

    @JsonProperty("description")
    String description;

    @JsonProperty("code")
    String code;
}
