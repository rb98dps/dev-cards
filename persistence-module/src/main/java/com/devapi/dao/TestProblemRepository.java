package com.devapi.dao;

import com.devapi.entities.TestProblem;
import com.devapi.entities.TestProblemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestProblemRepository extends JpaRepository<TestProblem, TestProblemId> {
}