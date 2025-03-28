package com.example.usedcarportal.repository;

import com.example.usedcarportal.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// This interface provides methods for querying and manipulating car data in the database.
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    // Retrieves all active car listings.
    List<Car> findByActiveTrue();

    // Retrieves all cars posted by a specific user and filters by active status.
    List<Car> findByPostedByAndActive(String postedBy, boolean active);

    // Retrieves all cars posted by a specific user.
    List<Car> findByPostedBy(String postedBy);

    // Searches for cars by make or model, ignoring case sensitivity.
    List<Car> findByMakeContainingIgnoreCaseOrModelContainingIgnoreCase(String make, String model);

    // Retrieves all cars by their registration year.
    List<Car> findByYear(Integer year);

    // Retrieves all cars with a price less than or equal to the specified value.
    List<Car> findByPriceLessThanEqual(Double price);
}
