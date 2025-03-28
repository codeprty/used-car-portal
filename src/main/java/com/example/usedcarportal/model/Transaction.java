package com.example.usedcarportal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Entity representing a completed car transaction.
@Entity
@Table(name = "transactions") // Maps this class to the "transactions" table in the database
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the transaction

    private Long carId; // ID of the car involved in the transaction
    private String sellerUsername; // Username of the seller
    private String buyerUsername; // Username of the buyer
    private double salePrice; // Final sale price of the car
    private LocalDateTime saleDate; // Date and time when the transaction was completed

    // No-args constructor (Required by JPA)
    public Transaction() {}

    // Constructor to create a new transaction./
    public Transaction(Long carId, String sellerUsername, String buyerUsername, double salePrice) {
        this.carId = carId;
        this.sellerUsername = sellerUsername;
        this.buyerUsername = buyerUsername;
        this.salePrice = salePrice;
        this.saleDate = LocalDateTime.now(); // Set the sale date to the current time
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }

    public String getSellerUsername() { return sellerUsername; }
    public void setSellerUsername(String sellerUsername) { this.sellerUsername = sellerUsername; }

    public String getBuyerUsername() { return buyerUsername; }
    public void setBuyerUsername(String buyerUsername) { this.buyerUsername = buyerUsername; }

    public double getSalePrice() { return salePrice; }
    public void setSalePrice(double salePrice) { this.salePrice = salePrice; }

    public LocalDateTime getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDateTime saleDate) { this.saleDate = saleDate; }
}
