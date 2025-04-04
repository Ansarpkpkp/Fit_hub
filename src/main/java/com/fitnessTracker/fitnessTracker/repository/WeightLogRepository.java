package com.fitnessTracker.fitnessTracker.repository;

import com.fitnessTracker.fitnessTracker.model.WeightLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeightLogRepository extends JpaRepository<WeightLog, Long> {

    List<WeightLog> findByUser_IdAndLogDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    List<WeightLog> findByUser_Id(Long userId);
}