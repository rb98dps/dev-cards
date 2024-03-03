package com.devapi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "user_problem", schema = "dev_cards")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProblem {

    @EmbeddedId
    private UserProblemId id;

    @MapsId("problemId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id",insertable = false, updatable = false)
    private Test test;

    @Column(name = "attempted", nullable = false)
    @Builder.Default
    private Boolean attempted = false;

    @Column(name = "option_selected")
    private String optionSelected;

    @Column(name = "attempt_time")
    private Integer attemptTime;

    @Column(name = "attempt_date")
    private Timestamp attemptDate;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}