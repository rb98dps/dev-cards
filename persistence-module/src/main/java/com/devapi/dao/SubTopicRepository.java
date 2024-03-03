package com.devapi.dao;

import com.devapi.entities.SubTopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubTopicRepository extends JpaRepository<SubTopic, UUID> {
}
