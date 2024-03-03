package com.devapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "test_problem", schema = "dev_cards")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestProblem {
    @EmbeddedId
    private TestProblemId id;

    @MapsId("problemId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @MapsId("testId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    @JsonIgnore
    private Test test;

    @Column(name = "order_no", nullable = false)
    private Integer orderNo;

}