package com.devapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "mcq")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Mcq {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "mcq_text", nullable = false)
    @NonNull
    private String mcqText;

    @Column(name = "answer_type", nullable = false)
    @NonNull
    private Boolean answerType = false;

    @Column(name = "answer_desc", nullable = false)
    @NonNull
    private String answerDesc;

    @Column(name = "order_no", nullable = false)
    @NonNull
    private Integer orderNo;

    @ManyToMany
    @JoinTable(name = "problem_mcq",
            joinColumns = @JoinColumn(name = "mcq_id"),
            inverseJoinColumns = @JoinColumn(name = "problem_id"))
    @ToString.Exclude
    @JsonIgnore
    private List<Problem> problems = new ArrayList<>();

}
