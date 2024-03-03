package com.devapi.requestentities;

import lombok.Data;

import java.util.UUID;
@Data
public class CreateTestRequest {

    UUID userId;
    UUID subTopicId;
    int noOfQuestions;
    int time;

}
