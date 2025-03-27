package com.example.usedcarportal.controller;

import java.security.Principal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/post-car")
    public String showPostCarForm() {
        return "post-car"; 
    }

    @GetMapping("/about-us")
    public String aboutUs(Model model, Principal principal) {
        addUserRoleToModel(model, principal);
        return "about-us";
    }

    @GetMapping("/contact-us")
    public String contactUs(Model model, Principal principal) {
        addUserRoleToModel(model, principal);
        return "contact-us";
    }

    private void addUserRoleToModel(Model model, Principal principal) {
        if (principal != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("isAdmin", isAdmin);
        }
    }
}
