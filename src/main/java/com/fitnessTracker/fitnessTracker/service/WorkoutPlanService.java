package com.fitnessTracker.fitnessTracker.service;

import com.fitnessTracker.fitnessTracker.model.WorkoutPlan;
import java.util.List;

public interface WorkoutPlanService {
    WorkoutPlan createWorkoutPlan(WorkoutPlan workoutPlan);
    WorkoutPlan getWorkoutPlanById(Long id);
    WorkoutPlan updateWorkoutPlan(WorkoutPlan workoutPlan);
    void deleteWorkoutPlan(Long id);
    List<WorkoutPlan> getAllWorkoutPlans();
}