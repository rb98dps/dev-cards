package com.devapi.model.requestentities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class CreateSubTopicRequest {

    @JsonProperty("topic_id")
    UUID topicId;

    @JsonProperty("topic_name")
    String topicName;

    @JsonProperty("subtopic_name")
    String name;

    @JsonProperty("description")
    String description;
}
