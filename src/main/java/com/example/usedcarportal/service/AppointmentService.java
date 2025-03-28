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

    // ✅ Book an appointment only (no bid handling)
    @Transactional
    public void bookAppointment(Long carId, String username, LocalDate appointmentDate) {
        Appointment appointment = new Appointment();
        appointment.setCarId(carId);
        appointment.setUsername(username);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setStatus("Pending");
        appointmentRepository.save(appointment);
    }

    // ✅ Book appointment and place a bid
    @Transactional
    public void bookAppointmentAndBid(Long carId, String username, LocalDate appointmentDate, double bidAmount) {
        // Save appointment
        Appointment appointment = new Appointment();
        appointment.setCarId(carId);
        appointment.setUsername(username);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setStatus("Pending");
        appointmentRepository.save(appointment);

        // Save bid
        Bid bid = new Bid(carId, username, bidAmount);
        bidRepository.save(bid);
    }

    // ✅ Get all user appointments with car details
    public List<Appointment> getUserAppointments(String username) {
        List<Appointment> appointments = appointmentRepository.findByUsername(username);
        for (Appointment appointment : appointments) {
            Car car = carRepository.findById(appointment.getCarId()).orElse(null);
            appointment.setCar(car); // Attach car details
        }
        return appointments;
    }

    // ✅ Get appointments by status
    public List<Appointment> getAppointmentsByStatus(String status) {
        return appointmentRepository.findByStatus(status);
    }

    // ✅ Approve appointment and handle transaction if bid is sufficient
    @Transactional
    public void approveAppointment(Long appointmentId, Long bidId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        Bid bid = bidRepository.findById(bidId).orElse(null);

        if (appointment != null && bid != null) {
            Car car = carRepository.findById(appointment.getCarId()).orElse(null);

            if (car != null) {
                // Update appointment status
                appointment.setStatus("Approved");
                appointmentRepository.save(appointment);

                // ✅ Check if bid meets/exceeds car price
                if (bid.getBidAmount() >= car.getPrice()) {
                    // Mark car as sold
                    car.setActive(false);
                    carRepository.save(car);

                    // ✅ Save the sale transaction
                    Transaction transaction = new Transaction(
                            car.getId(),
                            car.getPostedBy(),
                            bid.getBidderUsername(),
                            bid.getBidAmount()
                    );
                    transactionRepository.save(transaction);
                }
            }
        }
    }

    // ✅ Deny appointment
    @Transactional
    public void denyAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Update status to "Denied"
        appointment.setStatus("Denied");
        appointmentRepository.save(appointment);
    }

    // ✅ Get appointments with highest bids sorted
    public List<Appointment> getAppointmentsWithBidsSorted() {
        List<Appointment> appointments = appointmentRepository.findAll();
        Map<Long, Appointment> carAppointmentMap = new HashMap<>();

        for (Appointment appointment : appointments) {
            Car car = carRepository.findById(appointment.getCarId()).orElse(null);
            appointment.setCar(car); // Attach car details

            // Fetch all bids for this car
            List<Bid> bids = bidRepository.findByCarId(appointment.getCarId());
            bids.sort((b1, b2) -> Double.compare(b2.getBidAmount(), b1.getBidAmount())); // Sort descending

            if (!bids.isEmpty()) {
                appointment.setHighestBid(bids.get(0)); // Store only the highest bid
            }

            // Ensure only one appointment per car (based on highest bid)
            if (!carAppointmentMap.containsKey(appointment.getCarId()) ||
                    (appointment.getHighestBid() != null &&
                            appointment.getHighestBid().getBidAmount() >
                                    carAppointmentMap.get(appointment.getCarId()).getHighestBid().getBidAmount())) {
                carAppointmentMap.put(appointment.getCarId(), appointment);
            }
        }

        return new ArrayList<>(carAppointmentMap.values()); // Return one appointment per car
    }

}
