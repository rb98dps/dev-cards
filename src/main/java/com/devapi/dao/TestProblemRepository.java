package com.devapi.dao;

import com.devapi.model.entities.TestProblem;
import com.devapi.model.entities.TestProblemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestProblemRepository extends JpaRepository<TestProblem, TestProblemId> {
}