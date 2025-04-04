package com.fitnessTracker.fitnessTracker.service;

import com.fitnessTracker.fitnessTracker.model.User;
import com.fitnessTracker.fitnessTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(User user) {
        // You can add validation or other logic here before saving
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean isEmailUnique(String email) {
        return userRepository.findByEmailIgnoreCase(email).isEmpty();
    }

    @Override
    public User findUserByEmailForCheck(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void validateEmailUnique(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            throw new IllegalStateException("Email already exists");
        }
    }
}