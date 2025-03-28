package com.example.usedcarportal.controller;

import com.example.usedcarportal.model.Car;
import com.example.usedcarportal.repository.CarRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/cars") // Maps all car-related admin requests to this controller
public class AdminCarController {

    private final CarRepository carRepository;

    // Constructor-based dependency injection for CarRepository
    public AdminCarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping // Handles GET requests to "/admin/cars"
    public String showCarListings(Model model) {
        List<Car> cars = carRepository.findAll(); // Fetch all car listings from the database
        model.addAttribute("cars", cars); // Add car list to the model for rendering in the view
        
        return "admin-cars"; // Load admin-cars.html to display the list of cars
    }

     // Redirect to Car Management Page when clicking "View"
    @GetMapping("/view/{id}") // Handles GET requests for viewing a specific car's details
    public String viewCarManagement(@PathVariable Long id, Model model) {
        Optional<Car> carOptional = carRepository.findById(id); // Fetch the car by its ID
        
        if (carOptional.isPresent()) {
            model.addAttribute("car", carOptional.get()); // Add car details to the model
            return "admin-car-management"; // Load the car management page with the selected car
        }
        
        return "redirect:/admin/cars"; // Redirect back to the car list if the car is not found
    }
}
