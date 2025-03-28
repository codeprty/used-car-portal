package com.example.usedcarportal.controller;

import com.example.usedcarportal.model.Appointment;
import com.example.usedcarportal.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/appointments") // Maps all appointment-related admin requests to this controller
public class AdminAppointmentController {

    private final AppointmentService appointmentService;

    // Constructor-based dependency injection for AppointmentService
    public AdminAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping // Handles GET requests to "/admin/appointments"
    public String showAdminAppointments(Model model) {
        // Fetches all appointments with the highest bids sorted
        List<Appointment> appointments = appointmentService.getAppointmentsWithBidsSorted();
        
        // Adds the list of appointments to the model for rendering in the view
        model.addAttribute("appointments", appointments);
        
        return "admin-appointments"; // Returns the admin appointments view template
    }
}
