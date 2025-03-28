package com.example.usedcarportal.model;

import jakarta.persistence.*;

/**
 * Entity representing a car listed for sale.
 */
@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;
    private String model;
    private int year;
    private double price;
    private String imageUrl;
    private boolean active = true;

    @Column(nullable = false)
    private String postedBy; // Stores user email of the seller

    // Constructors

    /** No-args constructor (Required by JPA) */
    public Car() {
    }

    /** Parameterized constructor */
    public Car(String make, String model, int year, double price, String imageUrl, boolean active) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
        this.imageUrl = imageUrl;
        this.active = active;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }
}
