package com.example.usedcarportal.controller;

import com.example.usedcarportal.model.Car;
import com.example.usedcarportal.repository.CarRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/car-management")
public class AdminCarManagementController {

    private final CarRepository carRepository;

    public AdminCarManagementController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // Show Car Management Page for a specific car
    @GetMapping("/view/{id}")
    public String showCarManagement(@PathVariable Long id, Model model) {
        Optional<Car> carOptional = carRepository.findById(id);
        carOptional.ifPresent(car -> model.addAttribute("car", car));
        return "admin-car-management"; // Load admin-car-management.html
    }

    // Update Car Activation Status
    @PostMapping("/update-status")
    public String updateCarStatus(@RequestParam Long id, @RequestParam boolean active) {
        Optional<Car> carOptional = carRepository.findById(id);
        if (carOptional.isPresent()) {
            Car car = carOptional.get();
            car.setActive(active);
            carRepository.save(car);
        }
        return "redirect:/admin/cars";
    }
}
