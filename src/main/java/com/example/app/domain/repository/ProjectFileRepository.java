package com.example.app.domain.repository;

import com.example.app.domain.entity.ProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectFileRepository extends JpaRepository<ProjectFile, Integer> {
}
