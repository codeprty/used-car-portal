package com.example.usedcarportal.controller;

import com.example.usedcarportal.model.Appointment;
import com.example.usedcarportal.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/appointments")
public class AdminAppointmentController {

    private final AppointmentService appointmentService;

    public AdminAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public String showAdminAppointments(Model model) {
        List<Appointment> appointments = appointmentService.getAppointmentsWithBidsSorted();
        model.addAttribute("appointments", appointments);
        
        return "admin-appointments";
    }
}
