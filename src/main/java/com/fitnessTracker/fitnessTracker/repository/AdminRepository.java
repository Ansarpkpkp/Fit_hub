package com.fitnessTracker.fitnessTracker.repository;

import com.fitnessTracker.fitnessTracker.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmail(String email);  // Find admin by email (for login)
}