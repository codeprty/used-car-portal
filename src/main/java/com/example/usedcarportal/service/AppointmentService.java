package com.example.usedcarportal.service;

import com.example.usedcarportal.model.Appointment;
import com.example.usedcarportal.model.Car;
import com.example.usedcarportal.repository.AppointmentRepository;
import com.example.usedcarportal.repository.CarRepository;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CarRepository carRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, CarRepository carRepository) {
        this.appointmentRepository = appointmentRepository;
        this.carRepository = carRepository;
    }

    // Book an appointment
    public boolean bookAppointment(Long carId, String username, LocalDate appointmentDate) {
        // Check if the user already has an appointment for this car
        List<Appointment> existingAppointments = appointmentRepository.findByCarId(carId);
        for (Appointment appt : existingAppointments) {
            if (appt.getUsername().equals(username) && appt.getAppointmentDate().equals(appointmentDate)) {
                return false; // Prevent duplicate bookings for the same date
            }
        }

        Appointment appointment = new Appointment();
        appointment.setCarId(carId);
        appointment.setUsername(username);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setStatus("Pending");

        appointmentRepository.save(appointment);
        return true;
    }

    // Get all appointments with car details
    public List<Appointment> getUserAppointments(String username) {
        List<Appointment> appointments = appointmentRepository.findByUsername(username);

        for (Appointment appointment : appointments) {
            Car car = carRepository.findById(appointment.getCarId()).orElse(null);
            appointment.setCar(car); // ✅ Attach Car details to Appointment
        }

        return appointments;
    }
}
