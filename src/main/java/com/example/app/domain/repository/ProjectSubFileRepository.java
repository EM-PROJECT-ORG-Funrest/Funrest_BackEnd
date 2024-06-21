package com.example.app.domain.repository;

import com.example.app.domain.entity.ProjectFile;
import com.example.app.domain.entity.ProjectSubFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectSubFileRepository extends JpaRepository<ProjectSubFile, Integer> {
    void deleteByProject_proCode(Integer savedProCode);
}
