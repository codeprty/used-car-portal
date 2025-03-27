package com.example.usedcarportal.controller;

import com.example.usedcarportal.dto.UserRegistrationDto;
import com.example.usedcarportal.model.User;
import com.example.usedcarportal.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping
    public String registerUser(@ModelAttribute("user") UserRegistrationDto userDto) {
        userService.registerUser(userDto);
        return "redirect:/login?success";
    }
    
 // **Show Edit Profile Page**
    @GetMapping("/edit-profile")
    public String showEditProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        model.addAttribute("user", user); // ✅ Ensure this line is present
        return "edit-profile"; 
    }

    // **Handle Profile Update**
    @PostMapping("/edit-profile")
    public String updateProfile(@ModelAttribute User updatedUser, @AuthenticationPrincipal UserDetails userDetails) {
        userService.updateUserProfile(userDetails.getUsername(), updatedUser);
        return "redirect:/home?profileUpdated"; // Redirect to home page
    }
}
