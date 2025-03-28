package com.example.usedcarportal.controller;

import com.example.usedcarportal.model.Appointment;
import com.example.usedcarportal.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller // Marks this class as a Spring MVC Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    // Constructor-based dependency injection for AppointmentService
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Handle appointment booking and bidding together.
    @PostMapping("/book") // Handles POST request to book an appointment and place a bid
    public String bookAppointment(@RequestParam Long carId,
                                  @RequestParam LocalDate appointmentDate,
                                  @RequestParam double bidAmount,
                                  Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Redirect user to login if not authenticated
        }

        // Always allow saving the bid and appointment
        appointmentService.bookAppointmentAndBid(carId, principal.getName(), appointmentDate, bidAmount);

        return "redirect:/home"; // Redirect to home page after saving
    }

    // View all pending appointments (Admin View).
    @GetMapping("/admin") // Handles GET request to display pending appointments for admin
    public String viewPendingAppointments(Model model) {
        List<Appointment> pendingAppointments = appointmentService.getAppointmentsWithBidsSorted(); // Fetch sorted pending appointments
        model.addAttribute("appointments", pendingAppointments); // Add data to the model
        return "admin-appointments"; // Load admin-appointments.html
    }

    // Approve an appointment (Admin Action).
    @PostMapping("/approve") // Handles POST request to approve an appointment
    public String approveAppointment(@RequestParam Long appointmentId, 
                                     @RequestParam(required = false) Long bidId) {
        appointmentService.approveAppointment(appointmentId, bidId); // Approve appointment and link to highest bid
        return "redirect:/admin/appointments"; // Redirect to refresh the admin page
    }

    // Deny an appointment (Admin Action).
    @PostMapping("/deny/{id}") // Handles POST request to deny an appointment
    public String denyAppointment(@PathVariable Long id) {
        appointmentService.denyAppointment(id); // Mark appointment as denied
        return "redirect:/appointments/admin"; // Refresh admin page with updated data
    }
}
