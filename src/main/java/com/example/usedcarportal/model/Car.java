package com.example.usedcarportal.model;

import jakarta.persistence.*;

/**
 * Entity representing a car listed for sale.
 */
@Entity
@Table(name = "cars") // Maps this class to the "cars" table in the database
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the car

    private String make; // The make (brand) of the car
    private String model; // The model of the car
    private int year; // The manufacturing year of the car
    private double price; // The price of the car
    private String imageUrl; // URL to the image of the car
    private boolean active = true; // Indicates whether the car listing is active

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
        return id; // Getter for car ID
    }

    public void setId(Long id) {
        this.id = id; // Setter for car ID
    }

    public String getMake() {
        return make; // Getter for car make
    }

    public void setMake(String make) {
        this.make = make; // Setter for car make
    }

    public String getModel() {
        return model; // Getter for car model
    }

    public void setModel(String model) {
        this.model = model; // Setter for car model
    }

    public int getYear() {
        return year; // Getter for car year
    }

    public void setYear(int year) {
        this.year = year; // Setter for car year
    }

    public double getPrice() {
        return price; // Getter for car price
    }

    public void setPrice(double price) {
        this.price = price; // Setter for car price
    }

    public String getImageUrl() {
        return imageUrl; // Getter for car image URL
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl; // Setter for car image URL
    }

    public boolean isActive() {
        return active; // Getter for active status of the car listing
    }

    public void setActive(boolean active) {
        this.active = active; // Setter for active status of the car listing
    }

    public String getPostedBy() {
        return postedBy; // Getter for the email of the user who posted the car
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy; // Setter for the email of the user who posted the car
    }
}
