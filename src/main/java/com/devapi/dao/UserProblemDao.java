package com.devapi.dao;

import com.devapi.model.entities.Problem;
import com.devapi.model.entities.UserProblem;
import com.devapi.model.entities.UserProblemId;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UserProblemDao extends Dao<UserProblem, UserProblemId> {

    UserProblemDao(){
        super();
        this.setClazz(UserProblem.class);
    }


    public List<Problem> getProblemsNotinProgress(UUID userId, int numberOfProblems){
        String jpql = "SELECT p FROM Problem p " +
                "WHERE p NOT IN " +
                "(SELECT up.problem FROM UserProblem up WHERE up.id.userId = :userId) " +
                "ORDER BY p.id ASC";
        TypedQuery<Problem> query = entityManager.createQuery(jpql, Problem.class)
                .setParameter("userId", userId)
                .setMaxResults(numberOfProblems*3);

        return query.getResultList();
    }
}