package com.example.app.domain.repository;

import com.example.app.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
// <T:엔터티 클래스 타입. ID: 엔터티 클래스의 ID 타입>
@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Optional<Project> findByProCode(int proCode);

}
