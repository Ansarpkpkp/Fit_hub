package com.fitnessTracker.fitnessTracker.repository;

import com.fitnessTracker.fitnessTracker.model.DietPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {
}