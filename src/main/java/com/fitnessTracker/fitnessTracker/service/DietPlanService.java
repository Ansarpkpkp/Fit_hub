package com.fitnessTracker.fitnessTracker.service;

import com.fitnessTracker.fitnessTracker.model.DietPlan;
import java.util.List;

public interface DietPlanService {
    DietPlan createDietPlan(DietPlan dietPlan);
    DietPlan getDietPlanById(Long id);
    DietPlan updateDietPlan(DietPlan dietPlan);
    void deleteDietPlan(Long id);
    List<DietPlan> getAllDietPlans();
}