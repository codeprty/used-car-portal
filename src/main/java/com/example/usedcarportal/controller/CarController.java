package com.example.usedcarportal.controller;

import com.example.usedcarportal.model.Appointment;
import com.example.usedcarportal.model.Car;
import com.example.usedcarportal.repository.CarRepository;
import com.example.usedcarportal.service.AppointmentService;
import com.example.usedcarportal.service.ImageUploadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class CarController {
    
    private final CarRepository carRepository;
    private final ImageUploadService imageUploadService;
    private final AppointmentService appointmentService;

    public CarController(CarRepository carRepository, ImageUploadService imageUploadService, AppointmentService appointmentService) {
        this.carRepository = carRepository;
        this.imageUploadService = imageUploadService;
        this.appointmentService = appointmentService;
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

    // Show all active car listings
    @GetMapping("/cars")
    public String showCarList(Model model) {
        List<Car> cars = carRepository.findByActiveTrue();
        model.addAttribute("cars", cars);
        return "cars"; // Load cars.html
    }

    // Show the car posting form
    @GetMapping("/post-car")
    public String showPostCarForm() {
        return "post-car"; // Load post-car.html
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
    
    @PostMapping("/book-appointment")
    public String bookAppointment(@RequestParam Long carId, @RequestParam String appointmentDate, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Ensure user is logged in
        }

        Car car = carRepository.findById(carId).orElse(null);
        if (car == null) {
            return "redirect:/cars?error=CarNotFound";
        }

        LocalDate date = LocalDate.parse(appointmentDate);
        boolean booked = appointmentService.bookAppointment(carId, principal.getName(), date);
        
        if (!booked) {
            return "redirect:/car-details/" + carId + "?error=AlreadyBooked";
        }

        return "redirect:/appointments"; // Redirect to appointments page
    }

    @GetMapping("/appointments")
    public String showAppointmentsPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Ensure user is logged in
        }

        String username = principal.getName();
        List<Appointment> userAppointments = appointmentService.getUserAppointments(username);

        model.addAttribute("appointments", userAppointments);
        return "appointments"; // Load appointments.html
    }

}
