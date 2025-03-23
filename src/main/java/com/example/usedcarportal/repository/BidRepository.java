package com.example.usedcarportal.repository;

import com.example.usedcarportal.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByCarId(Long carId); // Retrieve all bids for a specific car
}
