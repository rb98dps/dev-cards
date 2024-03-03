package com.devapi.requestentities;

import lombok.Data;

import java.util.HashMap;
import java.util.UUID;
@Data
public class UpdateTestRequest {

    UUID userId;
    UUID testId;
    UUID problemId;
    Integer timeTaken;
    boolean endTest;
    HashMap<UUID,Boolean> answers;
}
