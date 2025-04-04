package com.fitnessTracker.fitnessTracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank; // Import for validation
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "workout_plans")
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Workout plan name is required") // Validation: Not empty
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters") // Size constraint
    @Column(nullable = false)
    private String name;


    @Lob  // Use @Lob for larger text fields
    @NotBlank(message = "Workout plan description is required") // Validation
    @Size(min = 5, message = "Description must be at least 5 characters") // Size constraint
    @Column(nullable = false)
    private String description; // Details of the workout plan

    // Constructors, getters, and setters

    public WorkoutPlan() {}

    public WorkoutPlan(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and setters (using Lombok @Data is a good alternative)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}