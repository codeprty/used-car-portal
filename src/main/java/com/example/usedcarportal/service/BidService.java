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
    private final CarRepository carRepository; // ✅ Ensure CarRepository is injected

    public BidService(BidRepository bidRepository, CarRepository carRepository) { 
        this.bidRepository = bidRepository;
        this.carRepository = carRepository;
    }

    // ✅ Place a bid
    public void placeBid(Long carId, String bidderUsername, double bidAmount) {
        Bid bid = new Bid(carId, bidderUsername, bidAmount);
        bidRepository.save(bid);
    }

    // ✅ Get all bids for a specific car
    public List<Bid> getBidsForCar(Long carId) {
        List<Bid> bids = bidRepository.findByCarId(carId);

        // ✅ Debugging - Print fetched bids before returning
        System.out.println("Bids fetched from DB for Car ID " + carId + ": " + bids.size());

        // ✅ Fetch car once instead of inside loop
        Car car = carRepository.findById(carId).orElse(null);

        for (Bid bid : bids) {
            System.out.println("Bid: " + bid.getBidderUsername() + ", Amount: " + bid.getBidAmount());
            bid.setCarDetails(car);
        }

        return bids;
    }
}
