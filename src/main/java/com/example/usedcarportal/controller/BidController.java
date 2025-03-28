package com.example.usedcarportal.controller;

import com.example.usedcarportal.model.Bid;
import com.example.usedcarportal.model.Car;
import com.example.usedcarportal.repository.CarRepository;
import com.example.usedcarportal.service.BidService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/bids") // ✅ Standardize all bid URLs
public class BidController {

    private final BidService bidService;
    private final CarRepository carRepository;

    public BidController(BidService bidService, CarRepository carRepository) {
        this.bidService = bidService;
        this.carRepository = carRepository;
    }

    /**
     * ✅ Prevents 404 if user goes to /bids without specifying a carId.
     */
    @GetMapping
    public String showBidsPage(Model model) {
        model.addAttribute("error", "Please select a car to view bids.");
        return "bids"; // ✅ Ensure "bids.html" exists inside "templates/"
    }

    /**
     * ✅ Handles bid submission correctly.
     */
    @PostMapping("/submit")
    public String submitBid(@RequestParam Long carId,
                            @RequestParam double bidAmount,
                            Model model,
                            Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // ✅ Ensure user is logged in
        }

        // ✅ Save the bid
        bidService.placeBid(carId, principal.getName(), bidAmount);

        // ✅ Retrieve updated bids
        Car car = carRepository.findById(carId).orElse(null);
        if (car == null) {
            return "redirect:/home?error=CarNotFound";
        }

        List<Bid> bids = bidService.getBidsForCar(carId);

        // ✅ Debugging: Print fetched bids
        System.out.println("Bids for Car ID " + carId + ": " + bids.size());
        for (Bid bid : bids) {
            System.out.println("Bidder: " + bid.getBidderUsername() + ", Amount: " + bid.getBidAmount());
        }

        // ✅ Pass updated data to view
        model.addAttribute("car", car);
        model.addAttribute("bids", bids);

        return "bids"; // ✅ Correctly return bids.html instead of redirecting
    }

    /**
     * ✅ Displays bids for a specific car.
     */
    @GetMapping("/{carId}")
    public String viewBidsForCar(@PathVariable Long carId,
                                 Model model,
                                 Principal principal) {
        Car car = carRepository.findById(carId).orElse(null);

        if (car == null) {
            return "redirect:/home?error=CarNotFound";
        }

        if (!car.getPostedBy().equals(principal.getName())) {
            return "redirect:/home?error=Unauthorized";
        }

        List<Bid> bids = bidService.getBidsForCar(carId);

        model.addAttribute("car", car);
        model.addAttribute("bids", bids);

        return "bids"; // ✅ Ensure "bids.html" exists inside "templates/"
    }
}
