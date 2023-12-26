package com.devapi.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTestId implements Serializable {
    @Serial
    private static final long serialVersionUID = 3591120833562445109L;
    @Column(name = "user_id", nullable = false, length = 16)
    private UUID userId;

    @Column(name = "test_id", nullable = false, length = 16)
    private UUID testId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserTestId entity = (UserTestId) o;
        return Objects.equals(this.testId, entity.testId) &&
                Objects.equals(this.userId, entity.userId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(testId, userId);
    }

}