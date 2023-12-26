package com.devapi.model.requestentities;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.devapi.model.entities.Mcq}
 */
@Value
@Data
public class McqCreateRequest implements Serializable {
    String mcqText;
    Boolean answerType;
    String answerDesc;
    Integer orderNo;
}