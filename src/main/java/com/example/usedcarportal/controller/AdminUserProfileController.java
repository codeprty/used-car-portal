package com.example.usedcarportal.controller;

import com.example.usedcarportal.model.Role;
import com.example.usedcarportal.model.User;
import com.example.usedcarportal.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class AdminUserProfileController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder; // Inject Password Encoder

    public AdminUserProfileController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // ✅ Assign injected encoder
    }

    // ✅ Display User Profile Management Page
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("roles", Role.values()); // List all roles for dropdown
            return "admin-user-profile";
        }
        return "redirect:/admin/users"; // Redirect if user not found
    }

    // ✅ Save Updated User Details
    @PostMapping("/update")
    public String updateUser(@ModelAttribute User updatedUser) {
        Optional<User> existingUser = userRepository.findById(updatedUser.getId());
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setEmail(updatedUser.getEmail());
            user.setFullName(updatedUser.getFullName());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            user.setRole(updatedUser.getRole());

            // ✅ Update password only if it is provided
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }
    
    // ✅ Reset Password
    @PostMapping("/reset-password/{id}")
    public String resetPassword(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode("Password123")); // Set default password
            userRepository.save(user);
        }
        return "redirect:/admin/users/edit/" + id + "?resetSuccess=true"; // Redirect with success flag
    }
}
