package com.devapi.dao;

import com.devapi.entities.TopicProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicProgressRepository extends JpaRepository<TopicProgress,TopicProgress.TopicProgressId> {
}
