package com.example.usedcarportal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Entity representing a bid placed on a car.
@Entity
@Table(name = "bids") // Maps this class to the "bids" table in the database
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the bid

    private Long carId; // Stores the ID of the car being bid on
    private String bidderUsername; // Stores the username of the bidder
    private double bidAmount; // Stores the amount of the bid
    private LocalDateTime timestamp; // Stores the timestamp when the bid was placed

    @Transient // Not stored in the database
    private Car carDetails; // Holds the details of the car being bid on, not persisted in DB

    // No-args constructor (Required by JPA)
    public Bid() {
    }

    /** Constructor for creating a new bid */
    public Bid(Long carId, String bidderUsername, double bidAmount) {
        this.carId = carId;
        this.bidderUsername = bidderUsername;
        this.bidAmount = bidAmount;
        this.timestamp = LocalDateTime.now(); // Set timestamp when bid is created
    }

    // Getters and Setters

    public Long getId() {
        return id; // Getter for bid ID
    }

    public void setId(Long id) {
        this.id = id; // Setter for bid ID
    }

    public Long getCarId() {
        return carId; // Getter for car ID associated with the bid
    }

    public void setCarId(Long carId) {
        this.carId = carId; // Setter for car ID associated with the bid
    }

    public String getBidderUsername() {
        return bidderUsername; // Getter for bidder's username
    }

    public void setBidderUsername(String bidderUsername) {
        this.bidderUsername = bidderUsername; // Setter for bidder's username
    }

    public double getBidAmount() {
        return bidAmount; // Getter for bid amount
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount; // Setter for bid amount
    }

    public LocalDateTime getTimestamp() {
        return timestamp; // Getter for timestamp when the bid was placed
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp; // Setter for timestamp
    }

    public Car getCarDetails() {
        return carDetails; // Getter for car details
    }

    public void setCarDetails(Car carDetails) {
        this.carDetails = carDetails; // Setter for car details
    }
}
