package com.fitnessTracker.fitnessTracker.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private double height; // in cm

    private double weight; // in kg

    @Column(name = "subscription_status")
    private boolean subscriptionStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WeightLog> weightLogs;

    @ManyToOne(fetch = FetchType.EAGER) // Changed to ManyToOne
    @JoinColumn(name = "workout_plan_id", referencedColumnName = "id")
    private WorkoutPlan workoutPlan;

    @ManyToOne(fetch = FetchType.EAGER) // Changed to ManyToOne
    @JoinColumn(name = "diet_plan_id", referencedColumnName = "id")
    private DietPlan dietPlan;
    // Constructors, getters, and setters
    public User() {}

    public User(String email, String password, String firstName, String lastName, double height, double weight) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.height = height;
        this.weight = weight;
        this.subscriptionStatus = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(boolean subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public List<WeightLog> getWeightLogs() {
        return weightLogs;
    }

    public void setWeightLogs(List<WeightLog> weightLogs) {
        this.weightLogs = weightLogs;
    }

    public WorkoutPlan getWorkoutPlan() {
        return workoutPlan;
    }
    public String getFullName() {
        return firstName + " " + lastName;
    }
    public void setWorkoutPlan(WorkoutPlan workoutPlan) {
        this.workoutPlan = workoutPlan;
    }

    public DietPlan getDietPlan() {
        return dietPlan;
    }

    public void setDietPlan(DietPlan dietPlan) {
        this.dietPlan = dietPlan;
    }
}