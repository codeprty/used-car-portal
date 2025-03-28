package com.example.usedcarportal.model;

import jakarta.persistence.*;
import java.time.LocalDate;

// Entity representing an appointment for a car viewing.
@Entity
@Table(name = "appointments") // Maps this class to the "appointments" table in the database
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the appointment

    private Long carId; // Stores the ID of the car for the appointment
    private String username; // Stores the username of the user who made the appointment
    private LocalDate appointmentDate; // Stores the date of the appointment
    private String status; // Stores the status of the appointment (e.g., "Pending", "Confirmed")

    @Transient
    private Bid highestBid; // Holds the highest bid for this appointment, not persisted in DB

    @Transient
    private Car car; // Holds the car details for this appointment, not persisted in DB

    // Getters and Setters

    public Long getId() {
        return id; // Getter for appointment ID
    }

    public void setId(Long id) {
        this.id = id; // Setter for appointment ID
    }

    public Long getCarId() {
        return carId; // Getter for car ID
    }

    public void setCarId(Long carId) {
        this.carId = carId; // Setter for car ID
    }

    public String getUsername() {
        return username; // Getter for username
    }

    public void setUsername(String username) {
        this.username = username; // Setter for username
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate; // Getter for appointment date
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate; // Setter for appointment date
    }

    public String getStatus() {
        return status; // Getter for appointment status
    }

    public void setStatus(String status) {
        this.status = status; // Setter for appointment status
    }

    public Car getCar() {
        return car; // Getter for car details
    }

    public void setCar(Car car) {
        this.car = car; // Setter for car details
    }

    public Bid getHighestBid() {
        return highestBid; // Getter for the highest bid
    }

    public void setHighestBid(Bid highestBid) {
        this.highestBid = highestBid; // Setter for the highest bid
    }
}
