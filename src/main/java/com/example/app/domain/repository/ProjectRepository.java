package com.example.app.domain.repository;

import com.example.app.domain.dto.UserDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {    // <T:엔터티 클래스 타입. ID: 엔터티 클래스의 ID 타입>

    Project findByProCode(int proCode);

    List<Project> findAllByProStatusOrderByProCodeDesc(int proStatus);

    // 무한 스크롤
    Page<Project> findAllByProStatusOrderByProCodeDesc(Pageable pageable, int proStatus);

    // 카테고리 검색
    Page<Project> findAllByProCategoryAndProStatusOrderByProCodeDesc(String proCategory, Pageable pageable, int proStatus);

    // 카테고리 검색 - 오픈 예정
    List<Project> findAllByProStatusOrderByProCode(int proStatus);

    // 승인/미승인 프로젝트 검색
    Page<Project> findByProStatus(Integer proStatus, Pageable pageable);

    // 키워드별 검색
    @Query("SELECT p FROM Project p WHERE p.proName LIKE %:proName% AND p.proStatus = :proStatus")
    Page<Project> findByProNameContainingAndProStatus(@Param("proName") String proName, Pageable pageable, int proStatus);




    // Notify --------------------------------
    @Query("SELECT p.proStartDate FROM Project p WHERE p.proCode = :proCode")
    Optional<String> findProStartDateByProCode(@Param("proCode") int proCode);

    @Query("SELECT p.proNotifyCnt FROM Project p WHERE p.proCode = :proCode")
    int findProNotifyCntByProCode(@Param("proCode") int proCode);

    // MyPage --------------------------------
    List<Project> findByUserId(User user);

    Long countByUserId(User user);

}
