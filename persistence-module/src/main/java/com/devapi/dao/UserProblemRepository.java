package com.devapi.dao;


import com.devapi.entities.Test;
import com.devapi.entities.User;
import com.devapi.entities.UserProblem;
import com.devapi.entities.UserProblemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProblemRepository extends JpaRepository<UserProblem, UserProblemId> {
    void deleteUserProblemByUserAndTest(User user, Test test);
}
