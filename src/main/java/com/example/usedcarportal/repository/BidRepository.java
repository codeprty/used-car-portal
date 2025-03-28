package com.example.usedcarportal.repository;

import com.example.usedcarportal.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for managing Bid entities.
 */
public interface BidRepository extends JpaRepository<Bid, Long> {

    /**
     * Finds all bids placed for a specific car.
     *
     * @param carId The ID of the car.
     * @return A list of bids associated with the given car ID.
     */
    List<Bid> findByCarId(Long carId);

    /**
     * Finds all bids placed by a specific user for a particular car.
     *
     * @param carId          The ID of the car.
     * @param bidderUsername The username of the bidder.
     * @return A list of bids made by the user for the given car ID.
     */
    List<Bid> findByCarIdAndBidderUsername(Long carId, String bidderUsername);
}
