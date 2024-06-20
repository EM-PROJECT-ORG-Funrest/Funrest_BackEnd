package com.example.app.domain.repository;

import com.example.app.domain.entity.Visit;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface VisitRepository extends JpaRepository<Visit, Integer> {

    @Query("SELECT v FROM Visit v WHERE v.visitDate = :today")
    Visit findVisitByDate(LocalDate today);

}
