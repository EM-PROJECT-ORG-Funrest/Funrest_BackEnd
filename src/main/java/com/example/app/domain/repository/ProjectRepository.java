package com.example.app.domain.repository;

import com.example.app.domain.dto.UserDto;
import com.example.app.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {    // <T:엔터티 클래스 타입. ID: 엔터티 클래스의 ID 타입>

}
