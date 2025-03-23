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
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Auto-create default admin if not exists
    @PostConstruct
    public void createDefaultAdmin() {
        if (!userRepository.existsByRole(Role.ADMIN)) {
            User admin = new User(
                "admin@hificars.com",
                passwordEncoder.encode("admin123"), // Default password
                "Default Admin",
                "1234567890",
                Role.ADMIN
            );
            userRepository.save(admin);
            System.out.println("✅ Default Admin Created: admin@hificars.com / admin123");
        }
    }

    // ✅ Admin Dashboard Page
    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        return "admin-dashboard"; // Loads admin-dashboard.html
    }
}
