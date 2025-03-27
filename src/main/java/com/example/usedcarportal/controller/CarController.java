package com.example.usedcarportal.controller;

import com.example.usedcarportal.model.Appointment;
import com.example.usedcarportal.model.Car;
import com.example.usedcarportal.repository.CarRepository;
import com.example.usedcarportal.service.AppointmentService;
import com.example.usedcarportal.service.CarService;
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
    private final AppointmentService appointmentService;
    private final CarService carService;

    public CarController(CarRepository carRepository, ImageUploadService imageUploadService, AppointmentService appointmentService,  CarService carService) {
        this.carRepository = carRepository;
        this.imageUploadService = imageUploadService;
        this.appointmentService = appointmentService;
        this.carService = carService;
    }

    // Show the homepage with user-specific car listings
    @GetMapping("/home")
    public String showHomePage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Redirect to login if not authenticated
        }

        String username = principal.getName();
        List<Car> userCars = carRepository.findByPostedBy(username); // Fetch user's cars

        model.addAttribute("username", username);
        model.addAttribute("userCars", userCars);

        return "home"; // Load home.html
    }

    
    // Show all active car listings OR search for cars using filters
    @GetMapping("/cars")
    public String listCars(@RequestParam(required = false) String search, Model model) {
        List<Car> cars = carService.searchCars(search);
        model.addAttribute("cars", cars);
        return "cars"; // Load cars.html
    }

    // Handle the posting of a new car listing
    @PostMapping("/post-car")
    public String postCar(
            @RequestParam String make,
            @RequestParam String model,
            @RequestParam int year,
            @RequestParam double price,
            @RequestParam("imageFile") MultipartFile imageFile,
            Principal principal) {

        if (imageFile.isEmpty()) {
            return "redirect:/post-car?error=ImageRequired";
        }

        String imageUrl = imageUploadService.saveImage(imageFile);

        Car car = new Car();
        car.setMake(make);
        car.setModel(model);
        car.setYear(year);
        car.setPrice(price);
        car.setImageUrl(imageUrl);
        car.setActive(true);
        car.setPostedBy(principal.getName()); // Associate car with the logged-in user

        carRepository.save(car);
        return "redirect:/home"; // Redirect to home page
    }
    
    


    // Show the edit car form for a specific car
    @GetMapping("/edit-car/{id}")
    public String editCarForm(@PathVariable Long id, Model model, Principal principal) {
        Car car = carRepository.findById(id).orElse(null);
        if (car == null) {
            return "redirect:/home?error=CarNotFound";
        }

        if (!car.getPostedBy().equals(principal.getName())) {
            return "redirect:/home?error=UnauthorizedAccess";
        }

        model.addAttribute("car", car);
        return "edit-car"; // Load edit-car.html
    }

    // Handle updates to an existing car listing
 // Handle Car Update
    @PostMapping("/edit-car/{id}")
    public String updateCar(
            @PathVariable Long id,
            @RequestParam String make,
            @RequestParam String model,
            @RequestParam int year,
            @RequestParam double price,
            @RequestParam("active") boolean active,
            @RequestParam("imageFile") MultipartFile imageFile,
            Principal principal) {

        Car car = carRepository.findById(id).orElse(null);
        if (car == null) {
            return "redirect:/home?error=CarNotFound";
        }

        if (!car.getPostedBy().equals(principal.getName())) {
            return "redirect:/home?error=UnauthorizedAccess";
        }

        car.setMake(make);
        car.setModel(model);
        car.setYear(year);
        car.setPrice(price);

        // Update image only if a new one is provided
        if (!imageFile.isEmpty()) {
            String imageUrl = imageUploadService.saveImage(imageFile);
            car.setImageUrl(imageUrl);
        }

        // Set active status from dropdown
        car.setActive(active);

        carRepository.save(car);
        return "redirect:/home";
    }
  
    // Show Car Details
    @GetMapping("/car-details/{id}")
    public String showCarDetails(@PathVariable Long id, Model model) {
        Car car = carRepository.findById(id).orElse(null);
        if (car == null) {
            return "redirect:/cars?error=CarNotFound";
        }

        model.addAttribute("car", car);
        return "car-details"; // Redirect to car-details.html
    }

    @GetMapping("/appointments")
    public String showAppointmentsPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Ensure user is logged in
        }

        String username = principal.getName();
        List<Appointment> userAppointments = appointmentService.getUserAppointments(username);

        model.addAttribute("appointments", userAppointments);
        return "redirect:/home"; // Load appointments.html
    }

}
