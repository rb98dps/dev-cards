package com.devapi.dao;

import com.devapi.model.entities.TopicProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicProgressRepository extends JpaRepository<TopicProgress,TopicProgress.TopicProgressId> {
}
