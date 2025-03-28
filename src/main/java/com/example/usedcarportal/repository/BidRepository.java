package com.example.usedcarportal.repository;

import com.example.usedcarportal.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// This interface allows CRUD operations for Bid data./
public interface BidRepository extends JpaRepository<Bid, Long> {

    // Finds all bids placed for a specific car.
    List<Bid> findByCarId(Long carId);

    // Finds all bids placed by a specific user for a particular car.
    List<Bid> findByCarIdAndBidderUsername(Long carId, String bidderUsername);
}
