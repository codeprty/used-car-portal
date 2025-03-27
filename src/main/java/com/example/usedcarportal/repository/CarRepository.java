package com.example.usedcarportal.repository;

import com.example.usedcarportal.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByActiveTrue(); // Get only active car listings
    List<Car> findByPostedByAndActive(String postedBy, boolean active);
    List<Car> findByPostedBy(String postedBy);

    List<Car> findByMakeContainingIgnoreCaseOrModelContainingIgnoreCase(String make, String model);
    List<Car> findByYear(Integer year);
    List<Car> findByPriceLessThanEqual(Double price);

}
