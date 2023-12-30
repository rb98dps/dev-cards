package com.devapi.dao;

import com.devapi.model.entities.User;
import com.devapi.model.entities.UserTest;
import com.devapi.model.entities.UserTestId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTestRepository extends JpaRepository<UserTest, UserTestId> {
    List<UserTest> findByEndedIsFalseOrEndedIsNullAndEndTimeIsNotNull();

    List<UserTest> findUserTestsByUserAndEndedIsTrue(User user);
}
