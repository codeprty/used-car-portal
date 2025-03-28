package com.example.usedcarportal.controller;

import com.example.usedcarportal.model.Car;
import com.example.usedcarportal.repository.CarRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping("/admin/car-management") // Maps admin car management requests
public class AdminCarManagementController {

    private final CarRepository carRepository;

    // Constructor-based dependency injection for CarRepository
    public AdminCarManagementController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // Show Car Management Page for a specific car
    @GetMapping("/view/{id}") // Handles GET request to display car management page
    public String showCarManagement(@PathVariable Long id, Model model) {
        Optional<Car> carOptional = carRepository.findById(id); // Fetch car by ID
        carOptional.ifPresent(car -> model.addAttribute("car", car)); // Add car to the model

        return "admin-car-management"; // Load admin-car-management.html
    }

    // Update Car Activation Status
    @PostMapping("/update-status") // Handles POST request to update car status
    public String updateCarStatus(@RequestParam Long id, @RequestParam boolean active) {
        Optional<Car> carOptional = carRepository.findById(id); // Fetch car by ID

        if (carOptional.isPresent()) {
            Car car = carOptional.get();
            car.setActive(active); // Update active status
            carRepository.save(car); // Save changes to the database
        }

        return "redirect:/admin/cars"; // Redirect back to the car listing page
    }
}
