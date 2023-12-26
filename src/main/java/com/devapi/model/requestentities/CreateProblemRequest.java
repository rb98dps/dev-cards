package com.devapi.model.requestentities;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateProblemRequest {
    private String name;


    private String description;


    private String code;


    private Integer problemMarks;


    private Boolean isMultiple;

    private List<McqCreateRequest> mcqList;

    private UUID subTopicId;
}
