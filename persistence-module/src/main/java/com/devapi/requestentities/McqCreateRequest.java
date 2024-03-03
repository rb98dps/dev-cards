package com.devapi.requestentities;

import com.devapi.entities.Mcq;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Mcq}
 */
@Value
@Data
public class McqCreateRequest implements Serializable {
    String mcqText;
    Boolean answerType;
    String answerDesc;
    Integer orderNo;
}