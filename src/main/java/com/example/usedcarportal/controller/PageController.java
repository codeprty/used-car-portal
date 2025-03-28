package com.example.usedcarportal.controller;

import java.security.Principal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // Handles GET request to display the car posting form
    @GetMapping("/post-car")
    public String showPostCarForm() {
        return "post-car"; // Return the post-car view template
    }

    // Handles GET request to display the About Us page
    @GetMapping("/about-us")
    public String aboutUs(Model model, Principal principal) {
        addUserRoleToModel(model, principal); // Add user role info to the model
        return "about-us"; // Return the about-us view template
    }

    // Handles GET request to display the Contact Us page
    @GetMapping("/contact-us")
    public String contactUs(Model model, Principal principal) {
        addUserRoleToModel(model, principal); // Add user role info to the model
        return "contact-us"; // Return the contact-us view template
    }

    // Adds the user's role (admin or not) to the model for role-based UI rendering
    private void addUserRoleToModel(Model model, Principal principal) {
        if (principal != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("isAdmin", isAdmin); // Store admin status in the model
        }
    }
}
