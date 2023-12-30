package com.devapi.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user_test")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTest implements Serializable {

    @EmbeddedId
    private UserTestId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @MapsId("testId")
    @JoinColumn(name = "test_id", referencedColumnName = "test_id")
    private Test test;

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "given_time", nullable = false)
    private Integer givenTime;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "problems_attempted", nullable = false)
    private Integer problemsAttempted;

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "is_ended")
    private boolean ended;

}
