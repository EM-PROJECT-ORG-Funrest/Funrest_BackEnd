package com.example.app.domain.repository;

import com.example.app.domain.entity.Notify;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, Integer> {

    // 알림 신청 중복 확인
    @Query("SELECT n FROM Notify n WHERE n.proCode = :project AND n.userId = :user")
    Optional<Notify> findByProCodeAndUserId(@Param("project") Project project, @Param("user") User user);
}
