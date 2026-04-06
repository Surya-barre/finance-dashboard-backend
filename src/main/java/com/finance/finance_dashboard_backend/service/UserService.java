package com.finance.finance_dashboard_backend.service;

import com.finance.finance_dashboard_backend.entity.User;
import com.finance.finance_dashboard_backend.exception.CustomException;
import com.finance.finance_dashboard_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
// Handles business logic for User operations
public class UserService {

    @Autowired
    private UserRepository repository;

    // Common method to get user or throw exception
    private User getUserOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException("User not found"));
    }

    // Create user
    public User createUser(User user) {

        if (user.getId() != null) {
            throw new CustomException("Id should not be provided. It is auto-generated");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new CustomException("Email already exists");
        }

        if (repository.existsByName(user.getName())) {
            throw new CustomException("Username already exists try another one");
        }

        return repository.save(user);
    }


    //Get all users with pagination
    public Page<User> getAllUsers(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    // Get all users Without pagination
    public List<User> getAllUsersWithoutPagination() {
        return repository.findAll();
    }

    // Full update
    public User updateUser(Long id, User user) {

        User existing = getUserOrThrow(id);

        // Email validation
        if (!existing.getEmail().equals(user.getEmail()) &&
                repository.existsByEmail(user.getEmail())) {
            throw new CustomException("Email already exists");
        }

        // Name validation
        if (!existing.getName().equals(user.getName()) &&
                repository.existsByName(user.getName())) {
            throw new CustomException("Username already exists");
        }

        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        existing.setRole(user.getRole());

        return repository.save(existing);
    }

    // Partial update
    public User patchUser(Long id, User user) {

        User existing = getUserOrThrow(id);

        // Name update
        if (user.getName() != null) {
            if (!existing.getName().equals(user.getName()) &&
                    repository.existsByName(user.getName())) {
                throw new CustomException("Username already exists");
            }
            existing.setName(user.getName());
        }

        // Email update
        if (user.getEmail() != null) {
            if (!existing.getEmail().equals(user.getEmail()) &&
                    repository.existsByEmail(user.getEmail())) {
                throw new CustomException("Email already exists");
            }
            existing.setEmail(user.getEmail());
        }

        // Role update
        if (user.getRole() != null) {
            existing.setRole(user.getRole());
        }

        // ACTIVE FIELD UPDATE - SPECIAL HANDLING
        if (user.getActive() != null) {

            // true → false not allowed here
            if (existing.getActive() && !user.getActive()) {
                throw new CustomException("Use deactivate API to deactivate user");
            }

            // false → true (reactivate allowed)
            if (!existing.getActive() && user.getActive()) {
                existing.setActive(true);
            }
        }

        return repository.save(existing);
    }

    // Deactivate user
    public User deactivateUser(Long id) {

        User existing = getUserOrThrow(id);

        existing.setActive(false);

        return repository.save(existing);
    }

    // Get user by ID
    public User getUserById(Long id) {
        return getUserOrThrow(id);
    }
}