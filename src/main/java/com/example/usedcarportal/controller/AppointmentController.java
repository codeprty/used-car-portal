package com.example.usedcarportal.controller;

import com.example.usedcarportal.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Handle appointment booking and bidding together
    @PostMapping("/book")
    public String bookAppointment(@RequestParam Long carId, 
                                  @RequestParam String appointmentDate, 
                                  @RequestParam double bidAmount, 
                                  Principal principal) {

        if (principal == null) {
            return "redirect:/login"; // Ensure the user is logged in
        }

        boolean success = appointmentService.bookAppointmentAndBid(carId, principal.getName(),
                LocalDate.parse(appointmentDate), bidAmount);

        if (!success) {
            return "redirect:/car-details/" + carId + "?error=AlreadyBooked";
        }

        return "redirect:/car-details/" + carId + "?success=AppointmentAndBidSaved";
    }
}
