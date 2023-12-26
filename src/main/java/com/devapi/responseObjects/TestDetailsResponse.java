package com.devapi.responseObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TestDetailsResponse {
    @JsonProperty("problems")
    List<ProblemDetailResponse> problemDetailResponseList;
    private Integer attempted;
    private Date startTime;
    private Integer totalProblems;
    private Integer totalScore;
    private Integer givenTime;
    private Date endTime;
}
