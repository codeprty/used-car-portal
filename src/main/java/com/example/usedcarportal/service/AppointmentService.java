package com.example.usedcarportal.service;

import com.example.usedcarportal.model.Appointment;
import com.example.usedcarportal.model.Bid;
import com.example.usedcarportal.model.Car;
import com.example.usedcarportal.repository.AppointmentRepository;
import com.example.usedcarportal.repository.BidRepository;
import com.example.usedcarportal.repository.CarRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final BidRepository bidRepository;
    private final CarRepository carRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, 
                              BidRepository bidRepository, 
                              CarRepository carRepository) {
        this.appointmentRepository = appointmentRepository;
        this.bidRepository = bidRepository;
        this.carRepository = carRepository;
    }

    // ✅ New method: Book an appointment only (no bid handling)
    @Transactional
    public void bookAppointment(Long carId, String username, LocalDate appointmentDate) {
        Appointment appointment = new Appointment();
        appointment.setCarId(carId);
        appointment.setUsername(username);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setStatus("Pending");
        appointmentRepository.save(appointment);
    }

    // ✅ Modified: Remove duplicate check since multiple bids are allowed
    @Transactional
    public void bookAppointmentAndBid(Long carId, String username, LocalDate appointmentDate, double bidAmount) {
        // Save appointment
        Appointment appointment = new Appointment();
        appointment.setCarId(carId);
        appointment.setUsername(username);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setStatus("Pending");
        appointmentRepository.save(appointment);

        // Save bid
        Bid bid = new Bid(carId, username, bidAmount);
        bidRepository.save(bid);
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
