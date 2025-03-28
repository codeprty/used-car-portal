package com.example.usedcarportal.repository;

import com.example.usedcarportal.model.Role;
import com.example.usedcarportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Provides CRUD operations for User entities and custom queries for user-specific actions.
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Finds a user by their email.
    Optional<User> findByEmail(String email);

    // Checks if a user with a specific role exists.
    boolean existsByRole(Role role);
}
