package com.example.usedcarportal.controller;

import com.example.usedcarportal.model.Appointment;
import com.example.usedcarportal.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

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

        return "redirect:/home"; // ✅ Redirect after saving
    }
    
    @GetMapping("/admin")
    public String viewPendingAppointments(Model model) {
        List<Appointment> pendingAppointments = appointmentService.getAppointmentsWithBidsSorted();
        model.addAttribute("appointments", pendingAppointments);
        return "admin-appointments";
    }

    @PostMapping("/approve")
    public String approveAppointment(@RequestParam Long appointmentId, @RequestParam(required = false) Long bidId) {
        appointmentService.approveAppointment(appointmentId, bidId);
        return "redirect:/admin/appointments";
    }

    @PostMapping("/deny/{id}")
    public String denyAppointment(@PathVariable Long id) {
        appointmentService.denyAppointment(id);
        return "redirect:/appointments/admin"; // ✅ Ensures admin page refreshes with updated data
    }

}
