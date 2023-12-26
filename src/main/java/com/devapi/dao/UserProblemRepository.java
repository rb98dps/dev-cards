package com.devapi.dao;

import com.devapi.model.entities.UserProblem;
import com.devapi.model.entities.UserProblemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProblemRepository extends JpaRepository<UserProblem, UserProblemId> {
}
