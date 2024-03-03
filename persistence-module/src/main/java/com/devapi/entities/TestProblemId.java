package com.devapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class TestProblemId implements Serializable {
    @Serial
    private static final long serialVersionUID = 3863925532423451819L;
    @Column(name = "test_id", nullable = false, length = 16)
    private UUID testId;

    @Column(name = "problem_id", nullable = false, length = 16)
    private UUID problemId;

    @Override
    public boolean equals(Object o) {
        TestProblemId entity = (TestProblemId) o;
        return Objects.equals(this.testId, entity.testId) &&
                Objects.equals(this.problemId, entity.problemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testId, problemId);
    }

}