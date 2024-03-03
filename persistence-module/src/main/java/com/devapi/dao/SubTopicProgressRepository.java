package com.devapi.dao;

import com.devapi.entities.SubTopicProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubTopicProgressRepository extends JpaRepository<SubTopicProgress,SubTopicProgress.SubTopicProgressId> {
}
