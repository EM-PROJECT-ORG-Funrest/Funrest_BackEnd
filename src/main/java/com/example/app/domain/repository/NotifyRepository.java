package com.example.app.domain.repository;

import com.example.app.domain.entity.Notify;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, Integer> {
    Long countByUserId(User user);

    // 신청한 알림 조회
    @Query("SELECT n FROM Notify n WHERE n.proCode = :project AND n.userId = :user")
    Optional<Notify> findByProCodeAndUserId(@Param("project") Project project, @Param("user") User user);

    // 사용자 Id로 신청한 알림 조회
    List<Notify> findAllByUserId(User user);

    Integer deleteByNotifyCode(int notifyCode);
}
