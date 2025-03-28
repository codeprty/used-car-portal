package com.example.usedcarportal.repository;

import com.example.usedcarportal.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// This interface allows CRUD operations for Appointment data.
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Finds all appointments booked by a specific user.
    List<Appointment> findByUsername(String username);

    // Finds all appointments related to a specific car.
    List<Appointment> findByCarId(Long carId);

    // Finds all appointments with a specific status.
    List<Appointment> findByStatus(String status);
}
