package com.example.usedcarportal.repository;

import com.example.usedcarportal.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for managing Appointment entities.
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * Finds all appointments booked by a specific user.
     *
     * @param username The username of the user.
     * @return A list of appointments associated with the given username.
     */
    List<Appointment> findByUsername(String username);

    /**
     * Finds all appointments related to a specific car.
     *
     * @param carId The ID of the car.
     * @return A list of appointments for the given car ID.
     */
    List<Appointment> findByCarId(Long carId);

    /**
     * Finds all appointments with a specific status.
     *
     * @param status The status of the appointment (e.g., "Pending", "Approved").
     * @return A list of appointments matching the given status.
     */
    List<Appointment> findByStatus(String status);
}
