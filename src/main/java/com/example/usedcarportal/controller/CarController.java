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

    public CarController(CarRepository carRepository,
                         ImageUploadService imageUploadService,
                         AppointmentService appointmentService,
                         CarService carService) {
        this.carRepository = carRepository;
        this.imageUploadService = imageUploadService;
        this.appointmentService = appointmentService;
        this.carService = carService;
    }

    // Show the homepage with car listings posted by the logged-in user
    @GetMapping("/home")
    public String showHomePage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Redirect to login page if user is not authenticated
        }

        String username = principal.getName();
        List<Car> userCars = carRepository.findByPostedBy(username); // Fetch cars posted by the user

        model.addAttribute("username", username);
        model.addAttribute("userCars", userCars);

        return "home"; // Load home.html page
    }

    // Show all active car listings or filter based on search criteria
    @GetMapping("/cars")
    public String listCars(@RequestParam(required = false) String search, Model model) {
        List<Car> cars = carService.searchCars(search); // Fetch cars based on search filters
        model.addAttribute("cars", cars);
        return "cars"; // Load cars.html page
    }

    // Handle posting of a new car listing with image upload
    @PostMapping("/post-car")
    public String postCar(@RequestParam String make,
                          @RequestParam String model,
                          @RequestParam int year,
                          @RequestParam double price,
                          @RequestParam("imageFile") MultipartFile imageFile,
                          Principal principal) {

        if (imageFile.isEmpty()) {
            return "redirect:/post-car?error=ImageRequired"; // Redirect if no image is uploaded
        }

        String imageUrl = imageUploadService.saveImage(imageFile); // Save uploaded image

        Car car = new Car();
        car.setMake(make);
        car.setModel(model);
        car.setYear(year);
        car.setPrice(price);
        car.setImageUrl(imageUrl);
        car.setActive(true);
        car.setPostedBy(principal.getName()); // Set the car owner as the logged-in user

        carRepository.save(car); // Save car details to the database
        return "redirect:/home"; // Redirect to home page after successful post
    }

    // Show the edit form for a specific car
    @GetMapping("/edit-car/{id}")
    public String editCarForm(@PathVariable Long id, Model model, Principal principal) {
        Car car = carRepository.findById(id).orElse(null);
        if (car == null) {
            return "redirect:/home?error=CarNotFound"; // Redirect if car does not exist
        }

        if (!car.getPostedBy().equals(principal.getName())) {
            return "redirect:/home?error=UnauthorizedAccess"; // Prevent unauthorized edits
        }

        model.addAttribute("car", car);
        return "edit-car"; // Load edit-car.html page
    }

    // Handle updating an existing car listing
    @PostMapping("/edit-car/{id}")
    public String updateCar(@PathVariable Long id,
                            @RequestParam String make,
                            @RequestParam String model,
                            @RequestParam int year,
                            @RequestParam double price,
                            @RequestParam("active") boolean active,
                            @RequestParam("imageFile") MultipartFile imageFile,
                            Principal principal) {

        Car car = carRepository.findById(id).orElse(null);
        if (car == null) {
            return "redirect:/home?error=CarNotFound"; // Redirect if car does not exist
        }

        if (!car.getPostedBy().equals(principal.getName())) {
            return "redirect:/home?error=UnauthorizedAccess"; // Prevent unauthorized updates
        }

        car.setMake(make);
        car.setModel(model);
        car.setYear(year);
        car.setPrice(price);

        // Update car image only if a new image is uploaded
        if (!imageFile.isEmpty()) {
            String imageUrl = imageUploadService.saveImage(imageFile);
            car.setImageUrl(imageUrl);
        }

        car.setActive(active); // Update car status (active/inactive)

        carRepository.save(car); // Save updated car details
        return "redirect:/home"; // Redirect to home page
    }

    // Show details of a specific car
    @GetMapping("/car-details/{id}")
    public String showCarDetails(@PathVariable Long id, Model model) {
        Car car = carRepository.findById(id).orElse(null);
        if (car == null) {
            return "redirect:/cars?error=CarNotFound"; // Redirect if car does not exist
        }

        model.addAttribute("car", car);
        return "car-details"; // Load car-details.html page
    }

    // Show user's booked appointments
    @GetMapping("/appointments")
    public String showAppointmentsPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Redirect to login if user is not authenticated
        }

        String username = principal.getName();
        List<Appointment> userAppointments = appointmentService.getUserAppointments(username); // Fetch user-specific appointments

        model.addAttribute("appointments", userAppointments);
        return "redirect:/home"; // Load appointments.html page
    }
}
