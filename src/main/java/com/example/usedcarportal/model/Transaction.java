package com.example.usedcarportal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a completed car transaction.
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long carId;
    private String sellerUsername;
    private String buyerUsername;
    private double salePrice;
    private LocalDateTime saleDate;

    // No-args constructor (Required by JPA)
    public Transaction() {}

    /**
     * Constructor to create a new transaction.
     * 
     * @param carId          ID of the car being sold
     * @param sellerUsername Username of the seller
     * @param buyerUsername  Username of the buyer
     * @param salePrice      Final sale price of the car
     */
    public Transaction(Long carId, String sellerUsername, String buyerUsername, double salePrice) {
        this.carId = carId;
        this.sellerUsername = sellerUsername;
        this.buyerUsername = buyerUsername;
        this.salePrice = salePrice;
        this.saleDate = LocalDateTime.now();
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
