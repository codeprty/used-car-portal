package com.example.usedcarportal.model;

import jakarta.persistence.*;

// Entity representing a registered user in the system.
@Entity
@Table(name = "users") // Maps this class to the "users" table in the database
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the user

    @Column(nullable = false, unique = true)
    private String email; // User's email (must be unique)

    @Column(nullable = false)
    private String password; // User's hashed password

    @Column(nullable = false)
    private String fullName; // User's full name

    @Column(nullable = false)
    private String phoneNumber; // User's contact number

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // USER or ADMIN role

    // No-args constructor (Required by JPA)
    public User() {}

    // Constructor for creating a new user.
    public User(String email, String password, String fullName, String phoneNumber, Role role) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
