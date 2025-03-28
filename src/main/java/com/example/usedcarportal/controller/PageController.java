package com.example.usedcarportal.controller;

import java.security.Principal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    /**
     * Displays the car posting form.
     *
     * @return The name of the post-car view.
     */
    @GetMapping("/post-car")
    public String showPostCarForm() {
        return "post-car";
    }

    /**
     * Displays the About Us page.
     *
     * @param model     The model to store attributes.
     * @param principal The currently authenticated user.
     * @return The name of the about-us view.
     */
    @GetMapping("/about-us")
    public String aboutUs(Model model, Principal principal) {
        addUserRoleToModel(model, principal);
        return "about-us";
    }

    /**
     * Displays the Contact Us page.
     *
     * @param model     The model to store attributes.
     * @param principal The currently authenticated user.
     * @return The name of the contact-us view.
     */
    @GetMapping("/contact-us")
    public String contactUs(Model model, Principal principal) {
        addUserRoleToModel(model, principal);
        return "contact-us";
    }

    /**
     * Adds the user's role information to the model.
     *
     * @param model     The model to store attributes.
     * @param principal The currently authenticated user.
     */
    private void addUserRoleToModel(Model model, Principal principal) {
        if (principal != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("isAdmin", isAdmin);
        }
    }
}
