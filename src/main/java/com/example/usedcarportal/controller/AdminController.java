package com.example.usedcarportal.controller;

import com.example.usedcarportal.model.Role;
import com.example.usedcarportal.model.User;
import com.example.usedcarportal.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import jakarta.annotation.PostConstruct;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin") // Maps all admin-related requests
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor for injecting UserRepository and PasswordEncoder
    public AdminController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Auto-create a default admin if one does not exist.
    @PostConstruct
    public void createDefaultAdmin() {
        if (!userRepository.existsByRole(Role.ADMIN)) { // Check if an admin exists in the database
            User admin = new User(
                "admin@hificars.com",
                passwordEncoder.encode("admin123"), // Encrypt default password
                "Admin",
                "1234567890",
                Role.ADMIN // Assign admin role
            );
            userRepository.save(admin); // Save the admin user to the database
            System.out.println("✅ Default Admin Created: admin@hificars.com / admin123");
        }
    }

    // Load the Admin Dashboard Page.
    @GetMapping("/dashboard") // Handle GET request to load admin dashboard
    public String showAdminDashboard(Model model) {
        return "admin-dashboard"; // Return view for admin-dashboard.html
    }
}
