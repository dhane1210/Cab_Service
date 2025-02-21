package com.CabService.CabService.service;

import com.CabService.CabService.config.CustomerPrincipal;
import com.CabService.CabService.dto.BookingRequest;
import com.CabService.CabService.model.Bill;
import com.CabService.CabService.model.Booking;
import com.CabService.CabService.model.Customer;
import com.CabService.CabService.model.Driver;
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

    // Add a new booking
    public String addBooking(BookingRequest bookingRequest) {
        // Fetch the driver and customer from the database
        Driver driver = driverRepository.findById(bookingRequest.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        Customer customer = customerRepository.findById(bookingRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Create a new Booking object
        Booking booking = new Booking();
        booking.setAssignedDriver(driver); // Set the assigned driver
        booking.setCustomer(customer); // Set the customer
        booking.setStartLocation(bookingRequest.getStartLocation());
        booking.setEndLocation(bookingRequest.getEndLocation());
        booking.setDistance(bookingRequest.getDistance());
        booking.setFare(bookingRequest.getFare());
        booking.setStatus("Pending"); // Set the initial status

        // Save the booking
        bookingRepository.save(booking);

        return "Booking added successfully";
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
        double baseFare = adminService.calculateBaseFare(booking.getDistance());

        // Get the tax rate from the pricing configuration
        double taxRate = adminService.getPricingConfig().getTax();  // Assuming getTax() returns the tax rate
        double tax = adminService.calculateTax(baseFare, taxRate);  // Pass both baseFare and taxRate

        double discountRate = adminService.getPricingConfig().getDiscount();  // Assuming getDiscount() returns the discount rate
        double discount = adminService.calculateDiscount(baseFare, discountRate);

        return new Bill(baseFare, tax, discount);
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

