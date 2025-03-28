package com.example.usedcarportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    /**
     * Displays the login page.
     *
     * @return The name of the login view.
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
