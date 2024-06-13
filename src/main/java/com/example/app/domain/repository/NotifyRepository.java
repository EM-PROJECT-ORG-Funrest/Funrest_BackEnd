package com.example.app.domain.repository;

import com.example.app.domain.entity.ProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifyRepository extends JpaRepository<ProjectFile, Integer> {
    Long CountByUserId(String userId);
}
