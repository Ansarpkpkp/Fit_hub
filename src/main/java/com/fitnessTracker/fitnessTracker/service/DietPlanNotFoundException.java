package com.fitnessTracker.fitnessTracker.service;

public class DietPlanNotFoundException extends RuntimeException {
    public DietPlanNotFoundException(String message) {
        super(message);
    }
}