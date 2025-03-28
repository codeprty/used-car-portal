package com.example.usedcarportal.repository;

import com.example.usedcarportal.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for managing Car entities.
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    /**
     * Retrieves all active car listings.
     *
     * @return A list of active cars.
     */
    List<Car> findByActiveTrue();

    /**
     * Retrieves all cars posted by a specific user and filters by active status.
     *
     * @param postedBy The email of the user who posted the car.
     * @param active   The active status of the car.
     * @return A list of cars that match the criteria.
     */
    List<Car> findByPostedByAndActive(String postedBy, boolean active);

    /**
     * Retrieves all cars posted by a specific user.
     *
     * @param postedBy The email of the user who posted the car.
     * @return A list of cars posted by the given user.
     */
    List<Car> findByPostedBy(String postedBy);

    /**
     * Searches for cars by make or model, ignoring case sensitivity.
     *
     * @param make  The car make (e.g., Toyota, Honda).
     * @param model The car model (e.g., Camry, Accord).
     * @return A list of cars that match the make or model criteria.
     */
    List<Car> findByMakeContainingIgnoreCaseOrModelContainingIgnoreCase(String make, String model);

    /**
     * Retrieves all cars by their registration year.
     *
     * @param year The year of manufacture.
     * @return A list of cars manufactured in the specified year.
     */
    List<Car> findByYear(Integer year);

    /**
     * Retrieves all cars with a price less than or equal to the specified value.
     *
     * @param price The maximum price.
     * @return A list of cars that are within the specified price range.
     */
    List<Car> findByPriceLessThanEqual(Double price);
}
