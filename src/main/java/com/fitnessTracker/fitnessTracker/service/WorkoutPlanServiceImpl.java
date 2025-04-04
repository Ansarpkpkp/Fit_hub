package com.fitnessTracker.fitnessTracker.service;

import com.fitnessTracker.fitnessTracker.model.WorkoutPlan;
import com.fitnessTracker.fitnessTracker.repository.WorkoutPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import Transactional

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutPlanServiceImpl implements WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    public WorkoutPlanServiceImpl(WorkoutPlanRepository workoutPlanRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
    }

    @Override
    @Transactional // Add @Transactional
    public WorkoutPlan createWorkoutPlan(WorkoutPlan workoutPlan) {
        return workoutPlanRepository.save(workoutPlan);
    }

    @Override
    @Transactional(readOnly = true) // Add readOnly for read operations
    public WorkoutPlan getWorkoutPlanById(Long id) {
        Optional<WorkoutPlan> optionalWorkoutPlan = workoutPlanRepository.findById(id);
        return optionalWorkoutPlan.orElse(null); // OrElse handles the case where the plan isn't found
    }

    @Override
    @Transactional
    public WorkoutPlan updateWorkoutPlan(WorkoutPlan workoutPlan) {
        return workoutPlanRepository.save(workoutPlan);
    }

    @Override
    @Transactional
    public void deleteWorkoutPlan(Long id) {
        workoutPlanRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutPlan> getAllWorkoutPlans() {
        return workoutPlanRepository.findAll();
    }
}