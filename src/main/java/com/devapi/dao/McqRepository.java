package com.devapi.dao;

import com.devapi.model.entities.Mcq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface McqRepository extends JpaRepository<Mcq, UUID> {
}
