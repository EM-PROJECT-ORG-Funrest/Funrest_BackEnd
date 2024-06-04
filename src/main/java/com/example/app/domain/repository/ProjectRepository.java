package com.example.app.domain.repository;

import com.example.app.domain.dto.UserDto;
import com.example.app.domain.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {    // <T:엔터티 클래스 타입. ID: 엔터티 클래스의 ID 타입>


    Project findByProCode(int proCode);

    List<Project> findAllByOrderByProCodeDesc();

    // 무한 스크롤
    Page<Project> findAllByOrderByProCodeDesc(Pageable pageable);

    // 키워드 검색
    Page<Project> findByProNameLike(String proName,Pageable pageable);

    //전체 검색
    List<Project> findAll();

    // 키워드별 검색
    List<Project> findAllByProCategory(String proCategory);
}
