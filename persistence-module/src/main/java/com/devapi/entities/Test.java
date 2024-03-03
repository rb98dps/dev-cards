package com.devapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "test", schema = "dev_cards")
public class Test {
    @Id
    @Column(name = "test_id", nullable = false, length = 16)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "total_problems", nullable = false)
    private Integer totalProblems;

    @Column(name = "total_score", nullable = false)
    private Integer totalScore;

    @OneToMany(mappedBy = "test")
    private Set<TestProblem> testProblems = new LinkedHashSet<>();

    @OneToMany(mappedBy = "test")
    @JsonIgnore
    private Set<UserProblem> userProblems = new LinkedHashSet<>();

    @OneToMany(mappedBy = "test")
    @JsonIgnore
    private Set<UserTest> userTests = new LinkedHashSet<>();

    public void addTestProblem(TestProblem testProblem) {
        testProblems.add(testProblem);
    }
}