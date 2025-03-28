package com.example.usedcarportal.service;

import com.example.usedcarportal.dto.UserRegistrationDto;
import com.example.usedcarportal.model.Role;
import com.example.usedcarportal.model.User;
import com.example.usedcarportal.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Initialize password encoder
    }

    // Register a new user
    public User registerUser(UserRegistrationDto registrationDto) {
        User user = new User();
        user.setEmail(registrationDto.getEmail()); // Set user's email
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword())); // Encode password
        user.setFullName(registrationDto.getFullName()); // Set user's full name
        user.setPhoneNumber(registrationDto.getPhoneNumber()); // Set user's phone number
        user.setRole(Role.USER); // Default role is USER
        return userRepository.save(user); // Save and return the user
    }

    // Fetch User by Email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")); // If user not found, throw exception
    }

    // Update User Profile
    public void updateUserProfile(String email, User updatedUser) {
        User existingUser = getUserByEmail(email); // Get existing user by email
        existingUser.setFullName(updatedUser.getFullName()); // Update full name
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber()); // Update phone number
        userRepository.save(existingUser); // Save the updated user
    }
}
