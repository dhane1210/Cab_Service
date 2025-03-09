package com.CabService.CabService.service;

import com.CabService.CabService.config.CustomerPrincipal;
import com.CabService.CabService.dto.BookingRequest;
import com.CabService.CabService.model.*;
import com.CabService.CabService.repo.*;
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
     PriceConfigRepository priceConfigRepository;

    @Autowired
    private BillRepository billRepository;

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

    public PriceConfig getDefaultPriceConfig() {
        return priceConfigRepository.findById(1L) // Assuming there's only one config
                .orElseThrow(() -> new RuntimeException("Price configuration not found"));
    }

    private void createAndLinkBill(Booking booking) {
        PriceConfig priceConfig = getDefaultPriceConfig();

        double baseFare = priceConfig.getBaseFare();
        double waitingTimeCharge = booking.getWaitingTime() * priceConfig.getWaitingTimeCharge();
        double taxRate = priceConfig.getTaxRate();
        double discountRate = priceConfig.getDiscountRate();

        double taxes = baseFare * (taxRate / 100);
        double discount = baseFare * (discountRate / 100);

        double totalAmount = baseFare + waitingTimeCharge + taxes - discount;

        Bill bill = new Bill(booking, baseFare, waitingTimeCharge, taxes, discount, totalAmount);
        billRepository.save(bill);

        booking.setBill(bill);
        bookingRepository.save(booking);
    }


    @Transactional
    public String updatePriceConfig(PriceConfig priceConfig) {
        PriceConfig existingConfig = getDefaultPriceConfig();
        existingConfig.setBaseFare(priceConfig.getBaseFare());
        existingConfig.setWaitingTimeCharge(priceConfig.getWaitingTimeCharge());
        existingConfig.setTaxRate(priceConfig.getTaxRate());
        existingConfig.setDiscountRate(priceConfig.getDiscountRate());
        priceConfigRepository.save(existingConfig);
        return "Price configuration updated successfully";
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