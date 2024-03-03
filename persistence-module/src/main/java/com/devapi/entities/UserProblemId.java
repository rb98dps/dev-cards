package com.devapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class UserProblemId implements Serializable {
    private static final long serialVersionUID = -8515917601494909222L;
    @Column(name = "user_id", nullable = false, length = 16)
    private UUID userId;

    @Column(name = "problem_id", nullable = false, length = 16)
    private UUID problemId;

    @Column(name = "test_id", nullable = false, length = 16)
    private UUID testId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserProblemId entity = (UserProblemId) o;
        return Objects.equals(this.testId, entity.testId) &&
                Objects.equals(this.problemId, entity.problemId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testId, problemId, userId);
    }

}