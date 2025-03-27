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
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User registerUser(UserRegistrationDto registrationDto) {
        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setFullName(registrationDto.getFullName());
        user.setPhoneNumber(registrationDto.getPhoneNumber());
        user.setRole(Role.USER); // Default role is USER
        return userRepository.save(user);
    }
    
 // **Fetch User by Email**
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // **Update User Profile**
    public void updateUserProfile(String email, User updatedUser) {
        User existingUser = getUserByEmail(email);
        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        userRepository.save(existingUser);
    }
}
