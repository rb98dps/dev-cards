package com.devapi.responseObjects;

import lombok.*;

import java.util.Date;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserTestDetailsResponse {

    private Integer score;
    private Date startTime;
    private Integer totalProblems;
    private Integer totalScore;
    private Integer givenTime;
    private Set<String> subTopicName;
    private Set<String> topicName;
}
