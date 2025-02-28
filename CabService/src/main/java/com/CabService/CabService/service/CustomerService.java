package com.CabService.CabService.service;

import com.CabService.CabService.config.CustomerPrincipal;
import com.CabService.CabService.dto.BookingRequest;
import com.CabService.CabService.model.Bill;
import com.CabService.CabService.model.Booking;
import com.CabService.CabService.model.Customer;
import com.CabService.CabService.model.Driver;
import com.CabService.CabService.repo.BillRepository;
import com.CabService.CabService.repo.BookingRepository;
import com.CabService.CabService.repo.CustomerRepository;
import com.CabService.CabService.repo.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private BillRepository billRepository; // Add BillRepository

    // Add a new booking and create a bill
    @Transactional
    public String addBooking(BookingRequest bookingRequest) {
        // Fetch the driver and customer from the database
        Driver driver = driverRepository.findById(bookingRequest.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        Customer customer = customerRepository.findById(bookingRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Create a new Booking object
        Booking booking = new Booking();
        booking.setAssignedDriver(driver);
        booking.setCustomer(customer);
        booking.setStartLocation(bookingRequest.getStartLocation());
        booking.setEndLocation(bookingRequest.getEndLocation());
        booking.setDistance(bookingRequest.getDistance());
        booking.setFare(bookingRequest.getFare());
        booking.setStatus("Pending");
        booking.setWaitingTime(bookingRequest.getWaitingTime()); // Set waiting time
        // Save the booking
        Booking savedBooking = bookingRepository.save(booking);

        // Create and link a Bill to the booking
        createAndLinkBill(savedBooking);

        return "Booking added successfully";
    }

    // Create and link a Bill to the booking
    private void createAndLinkBill(Booking booking) {
        // Calculate billing details
        double baseFare = booking.getFare(); // Use the fare from the booking
        double waitingTimeCharge = 0.0; // Default waiting time charge
        double taxRate = 10.0; // Example tax rate (10%)
        double discountRate = 5.0; // Example discount rate (5%)

        double taxes = baseFare * (taxRate / 100); // Calculate tax
        double discount = baseFare * (discountRate / 100); // Calculate discount

        // Create a new Bill
        Bill bill = new Bill(booking, baseFare, waitingTimeCharge, taxes, discount);

        // Save the bill
        billRepository.save(bill);

        // Link the bill to the booking
        booking.setBill(bill);
        bookingRepository.save(booking); // Update the booking with the bill
    }

    // Get bookings by customer
    public List<Booking> getBookingsByCustomer(int customerId) {
        return bookingRepository.findByCustomer_CustomerId(customerId);
    }

    // Finalize booking
    public String finalizeBooking(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus("Finalized");
        bookingRepository.save(booking);
        return "Booking finalized successfully";
    }

    // Get bill
    public Bill getBill(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        return booking.getBill(); // Return the linked bill
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + customer.getRole().toUpperCase()));

        return new org.springframework.security.core.userdetails.User(
                customer.getUsername(),
                customer.getPassword(),
                authorities
        );
    }

    public List<Customer> getCustomer() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    public void deleteBooking(int bookingId) {
        bookingRepository.deleteById(bookingId);
    }
}