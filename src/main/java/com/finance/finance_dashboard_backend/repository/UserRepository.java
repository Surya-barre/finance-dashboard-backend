package com.finance.finance_dashboard_backend.repository;

import com.finance.finance_dashboard_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository for User database operations
public interface UserRepository extends JpaRepository<User, Long> {

    // Check if email already exists (used for validation)
    boolean existsByEmail(String email);

    // Check if username already exists
    boolean existsByName(String name);
}