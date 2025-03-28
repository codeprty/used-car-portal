package com.example.usedcarportal.controller;

import com.example.usedcarportal.model.Role;
import com.example.usedcarportal.model.User;
import com.example.usedcarportal.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller // Marks this class as a Spring MVC Controller
@RequestMapping("/admin/users")
public class AdminUserProfileController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder; // Inject Password Encoder for secure password handling

    // Constructor to initialize UserRepository and PasswordEncoder
    public AdminUserProfileController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Display User Profile Management Page.
    @GetMapping("/edit/{id}") // Handle GET request to edit user profile
    public String editUser(@PathVariable Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get()); // Add user data to the model
            model.addAttribute("roles", Role.values()); // Populate role dropdown
            return "admin-user-profile"; // Load admin-user-profile.html
        }
        return "redirect:/admin/users"; // Redirect if user not found
    }

    // Save Updated User Details.
    @PostMapping("/update") // Handle POST request for updating user details
    public String updateUser(@ModelAttribute User updatedUser) {
        Optional<User> existingUser = userRepository.findById(updatedUser.getId());
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setEmail(updatedUser.getEmail()); // Update email
            user.setFullName(updatedUser.getFullName()); // Update full name
            user.setPhoneNumber(updatedUser.getPhoneNumber()); // Update phone number
            user.setRole(updatedUser.getRole()); // Update role

            // Update password only if a new password is provided
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            userRepository.save(user); // Save updated user details
        }
        return "redirect:/admin/users"; // Redirect back to the user list
    }

    // Reset User Password to Default.
    @PostMapping("/reset-password/{id}") // Handle POST request for password reset
    public String resetPassword(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode("Password123")); // Set default password
            userRepository.save(user); // Save updated password
        }
        return "redirect:/admin/users/edit/" + id + "?resetSuccess=true"; // Redirect with success flag
    }
}
