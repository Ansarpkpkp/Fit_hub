package com.fitnessTracker.fitnessTracker.service;

import com.fitnessTracker.fitnessTracker.model.DietPlan;
import com.fitnessTracker.fitnessTracker.repository.DietPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DietPlanServiceImpl implements DietPlanService {

    private final DietPlanRepository dietPlanRepository;

    @Autowired
    public DietPlanServiceImpl(DietPlanRepository dietPlanRepository) {
        this.dietPlanRepository = dietPlanRepository;
    }

    @Override
    @Transactional
    public DietPlan createDietPlan(DietPlan dietPlan) {
        return dietPlanRepository.save(dietPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public DietPlan getDietPlanById(Long id) {
        Optional<DietPlan> optionalDietPlan = dietPlanRepository.findById(id);
        return optionalDietPlan.orElse(null); // OrElse handles the case where the plan isn't found
    }

    @Override
    @Transactional
    public DietPlan updateDietPlan(DietPlan dietPlan) {
        return dietPlanRepository.save(dietPlan);
    }

    @Override
    @Transactional
    public void deleteDietPlan(Long id) {
        dietPlanRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietPlan> getAllDietPlans() {
        return dietPlanRepository.findAll();
    }
}