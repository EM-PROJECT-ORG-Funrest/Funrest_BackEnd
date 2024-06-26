package com.example.app.domain.repository;

import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.ProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectFileRepository extends JpaRepository<ProjectFile, Integer> {

    // 프로젝트 코드 별 프로젝트 삭제
    void deleteByProject_proCode(Integer savedProCode);

}
