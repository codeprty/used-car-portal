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

    public List<Car> searchCars(String search) {
        if (search == null || search.trim().isEmpty()) {
            return carRepository.findAll();
        }

        search = search.trim();
        
        try {
            Integer year = Integer.parseInt(search);
            return carRepository.findByYear(year);
        } catch (NumberFormatException ignored) {}

        try {
            Double price = Double.parseDouble(search);
            return carRepository.findByPriceLessThanEqual(price);
        } catch (NumberFormatException ignored) {}

        return carRepository.findByMakeContainingIgnoreCaseOrModelContainingIgnoreCase(search, search);
    }

}
