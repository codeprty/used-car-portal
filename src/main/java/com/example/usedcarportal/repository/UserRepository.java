package com.example.usedcarportal.repository;

import com.example.usedcarportal.model.Role;
import com.example.usedcarportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email.
     * 
     * @param email the email of the user
     * @return an Optional containing the user if found, otherwise empty
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user with a specific role exists.
     * 
     * @param role the role to check
     * @return true if a user with the role exists, otherwise false
     */
    boolean existsByRole(Role role);
}
