package com.example.usedcarportal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a bid placed on a car.
 */
@Entity
@Table(name = "bids")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long carId;
    private String bidderUsername;
    private double bidAmount;
    private LocalDateTime timestamp;

    @Transient // Not stored in the database
    private Car carDetails;

    // Constructors

    /** No-args constructor (Required by JPA) */
    public Bid() {
    }

    /** Constructor for creating a new bid */
    public Bid(Long carId, String bidderUsername, double bidAmount) {
        this.carId = carId;
        this.bidderUsername = bidderUsername;
        this.bidAmount = bidAmount;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getBidderUsername() {
        return bidderUsername;
    }

    public void setBidderUsername(String bidderUsername) {
        this.bidderUsername = bidderUsername;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Car getCarDetails() {
        return carDetails;
    }

    public void setCarDetails(Car carDetails) {
        this.carDetails = carDetails;
    }
}
