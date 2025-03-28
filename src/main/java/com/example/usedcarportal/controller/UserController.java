package com.example.usedcarportal.controller;

import com.example.usedcarportal.dto.UserRegistrationDto;
import com.example.usedcarportal.model.User;
import com.example.usedcarportal.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Controller for handling user registration and profile management
@Controller
@RequestMapping("/register")
public class UserController {

    private final UserService userService; // Service for user-related operations

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Handles GET request to display the user registration form
    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto()); // Add empty DTO to model
        return "register"; // Return the registration view
    }

    // Handles POST request to register a new user
    @PostMapping
    public String registerUser(@ModelAttribute("user") UserRegistrationDto userDto) {
        userService.registerUser(userDto); // Register the user using the service
        return "redirect:/login?success"; // Redirect to login page with success message
    }

    // Handles GET request to display the edit profile page
    @GetMapping("/edit-profile")
    public String showEditProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername()); // Fetch user details
        model.addAttribute("user", user); // Add user data to model
        return "edit-profile"; // Return the edit-profile view
    }

    // Handles POST request to update user profile
    @PostMapping("/edit-profile")
    public String updateProfile(@ModelAttribute User updatedUser, @AuthenticationPrincipal UserDetails userDetails) {
        userService.updateUserProfile(userDetails.getUsername(), updatedUser); // Update user data
        return "redirect:/home?profileUpdated"; // Redirect to home with update confirmation
    }
}
