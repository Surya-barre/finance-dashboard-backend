package com.finance.finance_dashboard_backend.entity;

import com.finance.finance_dashboard_backend.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//Entity class representing a user in the system
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@gmail\\.com$",
            message = "Email must be a valid gmail address"
    )
    @Column(unique = true)
    private String email;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    private Role role;

    private Boolean active = true;
}