package com.finance.finance_dashboard_backend.controller;

import com.finance.finance_dashboard_backend.entity.User;
import com.finance.finance_dashboard_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/users")
// Controller to manage user accounts
public class UserController {

    @Autowired
    private UserService service;

    // Create new user
    @Operation(summary = "Create a new user")
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

        User savedUser = service.createUser(user);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // Get all users (optional pagination)
    @Operation(summary = "Get all users (optional pagination)",
            description = "Provide page and size query params for pagination, or omit for full list")
    @GetMapping
    public Object getAllUsers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        // If pagination params present
        if (page != null && size != null) {
            return service.getAllUsers(page, size);
        }

        // Else return all users
        return service.getAllUsersWithoutPagination();
    }

    // Update full user details
    @Operation(summary = "Update full user details")
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id,
                           @Valid @RequestBody User user) {
        return service.updateUser(id, user);
    }

    // Partial update user
    @Operation(summary = "Update specific fields of a user")
    @PatchMapping("/{id}")
    public User patchUser(@PathVariable Long id,
                          @RequestBody User user) {
        return service.patchUser(id, user);
    }

    // Deactivate user (soft delete: user is marked inactive, not removed from DB)
    @Operation(
            summary = "Deactivate a user account (soft delete)",
            description = "Marks user as inactive instead of deleting from database"
    )
    @PatchMapping("/{id}/deactivate")
    public User deactivateUser(@PathVariable Long id) {
        return service.deactivateUser(id);
    }

    // Get user by ID
    @Operation(summary = "Get user details by ID")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return service.getUserById(id);
    }
}