package com.fitnessTracker.fitnessTracker.service;

import com.fitnessTracker.fitnessTracker.model.Admin;

import java.util.Optional;

public interface AdminService {

    Optional<Admin> findAdminByEmail(String email);
}