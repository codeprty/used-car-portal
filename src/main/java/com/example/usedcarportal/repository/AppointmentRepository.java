package com.example.usedcarportal.repository;

import com.example.usedcarportal.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByUsername(String username);
    List<Appointment> findByCarId(Long carId);
    List<Appointment> findByStatus(String status);
}
