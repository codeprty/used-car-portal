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

	// Handle appointment booking
	@PostMapping("/book")
	public String bookAppointment(@RequestParam Long carId, @RequestParam String appointmentDate, Principal principal) {

		if (principal == null) {
			return "redirect:/login"; // Ensure the user is logged in
		}

		boolean success = appointmentService.bookAppointment(carId, principal.getName(),
				LocalDate.parse(appointmentDate));

		if (!success) {
			return "redirect:/car-details/" + carId + "?error=AlreadyBooked";
		}

		return "redirect:/car-details/" + carId + "?success=AppointmentBooked";
	}

	// Show user's booked appointments
	@GetMapping("/my-appointments")
	public String showUserAppointments(Model model, Principal principal) {
		if (principal == null) {
			return "redirect:/login";
		}

		List<Appointment> appointments = appointmentService.getUserAppointments(principal.getName());
		model.addAttribute("appointments", appointments);

		return "my-appointments"; // Load my-appointments.html
	}

}
