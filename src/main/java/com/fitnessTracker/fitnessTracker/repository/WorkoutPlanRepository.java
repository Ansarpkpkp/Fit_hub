package com.fitnessTracker.fitnessTracker.repository;

import com.fitnessTracker.fitnessTracker.model.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {
}