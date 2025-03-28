package com.example.usedcarportal.controller;

import com.example.usedcarportal.dto.UserRegistrationDto;
import com.example.usedcarportal.model.User;
import com.example.usedcarportal.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling user registration and profile management.
 */
@Controller
@RequestMapping("/register")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the user registration form.
     *
     * @param model The model to store attributes.
     * @return The registration page view.
     */
    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    /**
     * Handles user registration.
     *
     * @param userDto The DTO containing user registration data.
     * @return Redirect to the login page with success message.
     */
    @PostMapping
    public String registerUser(@ModelAttribute("user") UserRegistrationDto userDto) {
        userService.registerUser(userDto);
        return "redirect:/login?success";
    }

    /**
     * Displays the edit profile page.
     *
     * @param model        The model to store attributes.
     * @param userDetails  The currently authenticated user.
     * @return The edit-profile page view.
     */
    @GetMapping("/edit-profile")
    public String showEditProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        return "edit-profile";
    }

    /**
     * Handles profile updates.
     *
     * @param updatedUser  The updated user details.
     * @param userDetails  The currently authenticated user.
     * @return Redirect to the home page with a profile update confirmation.
     */
    @PostMapping("/edit-profile")
    public String updateProfile(@ModelAttribute User updatedUser, @AuthenticationPrincipal UserDetails userDetails) {
        userService.updateUserProfile(userDetails.getUsername(), updatedUser);
        return "redirect:/home?profileUpdated";
    }
}
