package com.example.usedcarportal.repository;

import com.example.usedcarportal.model.Role;
import com.example.usedcarportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	
    Optional<User> findByEmail(String email);
    
    boolean existsByRole(Role role);

}
