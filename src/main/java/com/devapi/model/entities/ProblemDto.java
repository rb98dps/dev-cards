package com.devapi.model.entities;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link Problem}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProblemDto implements Serializable {
    UUID id;
    String name;
    String description;
    String code;
    Integer problemMarks;
    Boolean isMultiple;
    List<McqDto> mcqs = new ArrayList<>();

    /**
     * DTO for {@link Mcq}
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class McqDto implements Serializable {
        UUID id;
        String mcqText;
        String answerDesc;
        Integer orderNo;
    }
}