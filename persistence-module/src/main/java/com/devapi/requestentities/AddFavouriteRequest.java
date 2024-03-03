package com.devapi.requestentities;

import lombok.Data;

import java.util.UUID;

@Data
public class AddFavouriteRequest {
    UUID topicId;
    UUID subtopicId;
    UUID userId;
}
