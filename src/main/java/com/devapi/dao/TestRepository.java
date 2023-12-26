package com.devapi.dao;

import com.devapi.model.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestRepository extends JpaRepository<Test, UUID> {
}
