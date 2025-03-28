package com.example.usedcarportal.service;

import com.example.usedcarportal.model.Appointment;
import com.example.usedcarportal.model.Bid;
import com.example.usedcarportal.model.Car;
import com.example.usedcarportal.model.Transaction;
import com.example.usedcarportal.repository.AppointmentRepository;
import com.example.usedcarportal.repository.BidRepository;
import com.example.usedcarportal.repository.CarRepository;
import com.example.usedcarportal.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final BidRepository bidRepository;
    private final CarRepository carRepository;
    private final TransactionRepository transactionRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            BidRepository bidRepository,
            CarRepository carRepository,
            TransactionRepository transactionRepository) {
        this.appointmentRepository = appointmentRepository;
        this.bidRepository = bidRepository;
        this.carRepository = carRepository;
        this.transactionRepository = transactionRepository;
    }

    // Book an appointment without handling any bids
    @Transactional
    public void bookAppointment(Long carId, String username, LocalDate appointmentDate) {
        Appointment appointment = new Appointment();
        appointment.setCarId(carId);
        appointment.setUsername(username);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setStatus("Pending");
        appointmentRepository.save(appointment); // Save the appointment
    }

    // Book appointment and place a bid on the car
    @Transactional
    public void bookAppointmentAndBid(Long carId, String username, LocalDate appointmentDate, double bidAmount) {
        // Save the appointment
        Appointment appointment = new Appointment();
        appointment.setCarId(carId);
        appointment.setUsername(username);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setStatus("Pending");
        appointmentRepository.save(appointment);

        // Save the bid for the car
        Bid bid = new Bid(carId, username, bidAmount);
        bidRepository.save(bid); // Save the bid
    }

    // Get all appointments for a specific user, with car details attached
    public List<Appointment> getUserAppointments(String username) {
        List<Appointment> appointments = appointmentRepository.findByUsername(username);
        for (Appointment appointment : appointments) {
            Car car = carRepository.findById(appointment.getCarId()).orElse(null);
            appointment.setCar(car); // Attach car details to appointment
        }
        return appointments;
    }

    // Get appointments based on the appointment status (e.g., Pending, Approved)
    public List<Appointment> getAppointmentsByStatus(String status) {
        return appointmentRepository.findByStatus(status); // Fetch appointments by status
    }

    // Approve the appointment and handle the transaction if the bid is sufficient
    @Transactional
    public void approveAppointment(Long appointmentId, Long bidId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        Bid bid = bidRepository.findById(bidId).orElse(null);

        if (appointment != null && bid != null) {
            Car car = carRepository.findById(appointment.getCarId()).orElse(null);

            if (car != null) {
                // Update appointment status to Approved
                appointment.setStatus("Approved");
                appointmentRepository.save(appointment);

                // Check if the bid meets or exceeds the car's price
                if (bid.getBidAmount() >= car.getPrice()) {
                    // Mark the car as sold and inactive
                    car.setActive(false);
                    carRepository.save(car);

                    // Create and save the transaction for the sale
                    Transaction transaction = new Transaction(
                            car.getId(),
                            car.getPostedBy(),
                            bid.getBidderUsername(),
                            bid.getBidAmount()
                    );
                    transactionRepository.save(transaction); // Save the transaction
                }
            }
        }
    }

    // Deny the appointment and set its status to "Denied"
    @Transactional
    public void denyAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Update the status to "Denied"
        appointment.setStatus("Denied");
        appointmentRepository.save(appointment); // Save the denied appointment
    }

    // Get all appointments with bids, sorted by the highest bid for each car
    public List<Appointment> getAppointmentsWithBidsSorted() {
        List<Appointment> appointments = appointmentRepository.findAll();
        Map<Long, Appointment> carAppointmentMap = new HashMap<>(); // Map to store appointments per car

        for (Appointment appointment : appointments) {
            Car car = carRepository.findById(appointment.getCarId()).orElse(null);
            appointment.setCar(car); // Attach car details to the appointment

            // Fetch all bids for the car
            List<Bid> bids = bidRepository.findByCarId(appointment.getCarId());
            bids.sort((b1, b2) -> Double.compare(b2.getBidAmount(), b1.getBidAmount())); // Sort bids in descending order

            if (!bids.isEmpty()) {
                appointment.setHighestBid(bids.get(0)); // Attach the highest bid to the appointment
            }

            // Ensure only one appointment per car (based on highest bid)
            if (!carAppointmentMap.containsKey(appointment.getCarId()) ||
                    (appointment.getHighestBid() != null &&
                            appointment.getHighestBid().getBidAmount() >
                                    carAppointmentMap.get(appointment.getCarId()).getHighestBid().getBidAmount())) {
                carAppointmentMap.put(appointment.getCarId(), appointment); // Store the appointment with the highest bid
            }
        }

        return new ArrayList<>(carAppointmentMap.values()); // Return one appointment per car
    }

}
