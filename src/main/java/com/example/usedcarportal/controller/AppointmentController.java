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
                                  @RequestParam LocalDate appointmentDate, 
                                  @RequestParam double bidAmount, 
                                  Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        // Always allow saving the bid and appointment
        appointmentService.bookAppointmentAndBid(carId, principal.getName(), appointmentDate, bidAmount);

        return "redirect:/appointments"; // ✅ Redirect after saving
    }

}
