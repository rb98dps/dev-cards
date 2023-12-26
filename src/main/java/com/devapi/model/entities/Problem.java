package com.devapi.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "problem", schema = "dev_cards")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Problem {
    @Id
    @Column(name = "id", nullable = false, length = 16)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Lob
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "marks", nullable = false)
    private Integer problemMarks;

    @Column(name = "is_multiple", nullable = false)
    private Boolean isMultiple = false;

    @ManyToMany(mappedBy = "problems", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Mcq> mcqs = new ArrayList<>();

    @OneToMany(mappedBy = "problem")
    @JsonIgnore
    private Set<UserProblem> userProblems = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subtopic_id", nullable = false)
    @JsonIgnore
    private SubTopic subTopic;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    @JsonIgnore
    private Topic topic;

    @OneToMany(mappedBy = "problem")
    @JsonIgnore
    private Set<TestProblem> testProblems = new LinkedHashSet<>();

}