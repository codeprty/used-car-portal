package com.example.usedcarportal.controller;

import com.example.usedcarportal.model.Car;
import com.example.usedcarportal.repository.CarRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/cars")
public class AdminCarController {

    private final CarRepository carRepository;

    public AdminCarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping
    public String showCarListings(Model model) {
        List<Car> cars = carRepository.findAll(); // Fetch all car listings
        model.addAttribute("cars", cars);
        
        return "admin-cars"; // Load admin-cars.html
    }

    /**
     * ✅ Redirect to Car Management Page when clicking "View"
     */
    @GetMapping("/view/{id}")
    public String viewCarManagement(@PathVariable Long id, Model model) {
        Optional<Car> carOptional = carRepository.findById(id);
        
        if (carOptional.isPresent()) {
            model.addAttribute("car", carOptional.get());
            return "admin-car-management"; // Load car management page with the selected car
        }
        
        return "redirect:/admin/cars"; // Redirect if car not found
    }
}
