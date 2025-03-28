package com.example.usedcarportal.controller;

import com.example.usedcarportal.model.User;
import com.example.usedcarportal.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller // Marks this class as a Spring MVC Controller
public class AdminUserController {

    private final UserRepository userRepository;

    // Constructor for injecting UserRepository
    public AdminUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Display the list of registered users.
    @GetMapping("/admin/users") // Handle GET request to display all users
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll(); // Fetch all users from the database
        model.addAttribute("users", users); // Add users to the model for the view
        return "admin-users"; // Return view for admin-users.html
    }

    // Delete a user by ID.
    @PostMapping("/admin/users/delete/{id}") // Handle POST request to delete a user
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id); // Delete user from the database
        return "redirect:/admin/users"; // Redirect back to the users list
    }
}
