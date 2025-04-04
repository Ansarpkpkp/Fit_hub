package com.fitnessTracker.fitnessTracker.service;

import com.fitnessTracker.fitnessTracker.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerUser(User user);

    Optional<User> findUserByEmail(String email);

    User updateUser(User user);

    void deleteUser(Long id);

    List<User> getAllUsers();

    Optional<User> findUserById(Long id);

    boolean isEmailUnique(String email);

    User findUserByEmailForCheck(String email);

    void validateEmailUnique(User user);
}