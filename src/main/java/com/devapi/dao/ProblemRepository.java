package com.devapi.dao;

import com.devapi.model.entities.Problem;
import com.devapi.model.entities.SubTopic;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface ProblemRepository extends JpaRepository<Problem, UUID> {
    public List<Problem> getProblemBySubTopicIs(@NonNull SubTopic subTopic);

    @Query("select p from Problem p inner join UserProblem up on p.id=up.id.problemId where " +
            "up.id.userId = ?1 and p.subTopic = ?2 and up.attemptDate < ?3 order by up.attemptDate")
    public List<Problem> getProblems(UUID userId, SubTopic subTopic, Timestamp timestamp);

    @Query("select p from Problem p inner join UserProblem up on p.id=up.id.problemId where " +
            "up.id.userId = ?1 and p.subTopic = ?2 and up.attemptDate >= ?3 order by up.attemptDate")
    public List<Problem> getProblems1(UUID userId, SubTopic subTopic, Timestamp timestamp);
}
