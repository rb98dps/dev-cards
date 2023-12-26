package com.devapi.dao;

import com.devapi.model.entities.UserTest;
import com.devapi.model.entities.UserTestId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTestRepository extends JpaRepository<UserTest, UserTestId> {
}
