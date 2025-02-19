package com.CabService.CabService.service;

import com.CabService.CabService.config.CustomerPrincipal;
import com.CabService.CabService.model.Bill;
import com.CabService.CabService.model.Booking;
import com.CabService.CabService.model.Customer;
import com.CabService.CabService.repo.BookingRepository;
import com.CabService.CabService.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    private AdminService adminService;

    // Add a new booking
    public String addBooking(Booking booking) {
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

    public Customer addCustomer(Customer customer) {
        customer.setPassword(encoder.encode(customer.getPassword()));
        return customerRepository.save(customer);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username);
        if(customer == null ){
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("User Not Found");
        }
        return new CustomerPrincipal(customer);
    }


    public List<Customer> getCustomer() {
        return customerRepository.findAll();
    }
}

