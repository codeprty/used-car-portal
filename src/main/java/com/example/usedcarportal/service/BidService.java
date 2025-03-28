package com.example.usedcarportal.service;

import com.example.usedcarportal.model.Bid;
import com.example.usedcarportal.model.Car;
import com.example.usedcarportal.repository.BidRepository;
import com.example.usedcarportal.repository.CarRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BidService {

    private final BidRepository bidRepository;
    private final CarRepository carRepository; // Ensure CarRepository is injected

    public BidService(BidRepository bidRepository, CarRepository carRepository) { 
        this.bidRepository = bidRepository;
        this.carRepository = carRepository;
    }

    // Place a bid on a specific car
    public void placeBid(Long carId, String bidderUsername, double bidAmount) {
        Bid bid = new Bid(carId, bidderUsername, bidAmount);
        bidRepository.save(bid); // Save the bid to the repository
    }

    // Get all bids placed on a specific car
    public List<Bid> getBidsForCar(Long carId) {
        List<Bid> bids = bidRepository.findByCarId(carId);

        // Debugging - Print fetched bids before returning
        System.out.println("Bids fetched from DB for Car ID " + carId + ": " + bids.size());

        // Fetch car details once instead of inside the loop
        Car car = carRepository.findById(carId).orElse(null);

        // Attach car details to each bid
        for (Bid bid : bids) {
            System.out.println("Bid: " + bid.getBidderUsername() + ", Amount: " + bid.getBidAmount());
            bid.setCarDetails(car); // Set car details on the bid object
        }

        return bids; // Return the list of bids
    }
}
