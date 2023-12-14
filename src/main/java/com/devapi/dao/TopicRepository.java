package com.devapi.dao;

import com.devapi.model.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
public interface TopicRepository extends JpaRepository<Topic, UUID> {

}
