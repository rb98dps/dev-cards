package com.devapi.dao;

import com.devapi.model.entities.SubTopicProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubTopicProgressRepository extends JpaRepository<SubTopicProgress,SubTopicProgress.SubTopicProgressId> {
}
