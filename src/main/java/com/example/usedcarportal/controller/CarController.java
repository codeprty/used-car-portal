package com.example.usedcarportal.controller;

import com.example.usedcarportal.model.Car;
import com.example.usedcarportal.repository.CarRepository;
import com.example.usedcarportal.service.ImageUploadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;
import java.util.List;

@Controller
public class CarController {
    private final CarRepository carRepository;
    private final ImageUploadService imageUploadService;

    public CarController(CarRepository carRepository, ImageUploadService imageUploadService) {
        this.carRepository = carRepository;
        this.imageUploadService = imageUploadService;
    }

    // Existing Car Listing Page
    @GetMapping("/cars")
    public String showCarList(Model model) {
        List<Car> cars = carRepository.findByActiveTrue();
        model.addAttribute("cars", cars);
        return "cars"; // This will render cars.html
    }

    // Show the Car Posting Form
    @GetMapping("/post-car")
    public String showPostCarForm() {
        return "post-car"; // This will render post-car.html
    }

    // Handle Car Posting
    @PostMapping("/post-car")
    public String postCar(
            @RequestParam String make,
            @RequestParam String model,
            @RequestParam int year,
            @RequestParam double price,
            @RequestParam("imageFile") MultipartFile imageFile,
            Principal principal) { // Get logged-in user's email
        
        String imageUrl = imageUploadService.saveImage(imageFile);

        Car car = new Car();
        car.setMake(make);
        car.setModel(model);
        car.setYear(year);
        car.setPrice(price);
        car.setImageUrl(imageUrl);
        car.setActive(true);
        car.setPostedBy(principal.getName()); // Save the user's email

        carRepository.save(car);
        return "redirect:/cars"; // Redirect to car listings after posting
    }
    
 // Show Edit Form (Only for the Car Owner)
    @GetMapping("/edit-car/{id}")
    public String editCarForm(@PathVariable Long id, Model model, Principal principal) {
    	Car car = carRepository.findById(id).orElse(null);
    	if (car == null) {
    	    return "redirect:/cars?error=CarNotFound";
    	}

        // Restrict access: Only owner can edit
        if (!car.getPostedBy().equals(principal.getName())) {
            return "redirect:/cars"; // Redirect unauthorized users
        }

        model.addAttribute("car", car);
        return "edit-car"; // Render edit-car.html
    }

    // Handle Car Update
    @PostMapping("/edit-car/{id}")
    public String updateCar(
            @PathVariable Long id,
            @RequestParam String make,
            @RequestParam String model,
            @RequestParam int year,
            @RequestParam double price,
            @RequestParam("imageFile") MultipartFile imageFile,
            Principal principal) {

    	Car car = carRepository.findById(id).orElse(null);
    	if (car == null) {
    	    return "redirect:/cars?error=CarNotFound";
    	}

        // Restrict update access
        if (!car.getPostedBy().equals(principal.getName())) {
            return "redirect:/cars";
        }

        // Update details
        car.setMake(make);
        car.setModel(model);
        car.setYear(year);
        car.setPrice(price);

        // Update image if provided
        if (!imageFile.isEmpty()) {
            String imageUrl = imageUploadService.saveImage(imageFile);
            car.setImageUrl(imageUrl);
        }

        carRepository.save(car);
        return "redirect:/cars"; // Redirect to listing after update
    }
    
    // Users can deactivate their own posts (not delete)
    @PostMapping("/deactivate-car/{id}")
    public String deactivateCar(@PathVariable Long id, Principal principal) {
        Car car = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not found"));

        // Only the owner can deactivate
        if (!car.getPostedBy().equals(principal.getName())) {
            return "redirect:/cars";
        }

        car.setActive(false); // Mark the car as inactive
        carRepository.save(car);
        return "redirect:/cars";
    }
    
    // Admin can delete a car
    @PostMapping("/admin/delete-car/{id}")
    public String deleteCar(@PathVariable Long id) {
        carRepository.deleteById(id);
        return "redirect:/cars";
    }
    
    @GetMapping("/my-listings")
    public String myListings(Model model, Principal principal) {
        String username = principal.getName();
        List<Car> activeCars = carRepository.findByPostedByAndActive(username, true);
        List<Car> inactiveCars = carRepository.findByPostedByAndActive(username, false);

        model.addAttribute("activeCars", activeCars);
        model.addAttribute("inactiveCars", inactiveCars);
        return "my-listings";
    }

}
