package com.example.usedcarportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Handle GET request to display the login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Return the login view template
    }
}
