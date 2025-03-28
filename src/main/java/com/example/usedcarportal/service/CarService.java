package com.example.usedcarportal.service;

import com.example.usedcarportal.model.Car;
import com.example.usedcarportal.repository.CarRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // Search cars by Make, Model, Year, or Price Range
    public List<Car> searchCars(String search) {
        if (search == null || search.trim().isEmpty()) {
            return carRepository.findAll(); // Return all cars if search is empty or null
        }

        search = search.trim(); // Trim whitespace from search input
        
        // Try parsing as Year
        try {
            Integer year = Integer.parseInt(search); // Parse search term as year
            return carRepository.findByYear(year); // Return cars matching the year
        } catch (NumberFormatException ignored) {}

        // Try parsing as Price
        try {
            Double price = Double.parseDouble(search); // Parse search term as price
            return carRepository.findByPriceLessThanEqual(price); // Return cars within price range
        } catch (NumberFormatException ignored) {}

        // Default: Search by Make or Model (case-insensitive)
        return carRepository.findByMakeContainingIgnoreCaseOrModelContainingIgnoreCase(search, search); // Return cars that match the search in either make or model
    }
}
