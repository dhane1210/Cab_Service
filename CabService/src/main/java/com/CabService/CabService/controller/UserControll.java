package com.CabService.CabService.controller;

import com.CabService.CabService.model.Customer;
import com.CabService.CabService.service.CustomerService;
import com.CabService.CabService.service.JWTService;
import com.CabService.CabService.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserControll {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserAuthService userAuthService;

    @PostMapping("/add-user")
    public ResponseEntity<Map<String, String>> addCustomer(@RequestBody Customer customer) {
        String role;
        if ("admin".equals(customer.getUsername()) && "admin".equals(customer.getPassword())) {
            role = "ADMIN"; // Set role to ADMIN
        } else {
            role = "CUSTOMER"; // Set role to CUSTOMER
        }
        customer.setRole(role); // Set the role in the customer object
        Customer savedCustomer = userAuthService.addUser(customer); // Save the user

        // Generate JWT token with username and role
        String token = jwtService.generateToken(customer.getUsername(), role);

        // Return token and user details
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("id", String.valueOf(savedCustomer.getCustomerId()));
        response.put("username", savedCustomer.getUsername());
        response.put("role", savedCustomer.getRole());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all-customer")
    public List<Customer> getCustomers() {
        return customerService.getCustomer();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Customer customer) {
        String token = userAuthService.verify(customer);
        if (token.equals("Fail")) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        // Fetch user details
        Customer loggedInUser = customerService.getCustomerByUsername(customer.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT token with username and role
        String newToken = jwtService.generateToken(loggedInUser.getUsername(), loggedInUser.getRole());

        // Return token and user details
        Map<String, String> response = new HashMap<>();
        response.put("token", newToken);
        response.put("id", String.valueOf(loggedInUser.getCustomerId()));
        response.put("username", loggedInUser.getUsername());
        response.put("role", loggedInUser.getRole());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-session")
    public ResponseEntity<?> checkSession(@RequestHeader("Authorization") String token) {
        try {
            // Extract the username from the token
            String username = jwtService.extractUserName(token.replace("Bearer ", ""));

            // Fetch the user details from the database
            Customer customer = customerService.getCustomerByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Return the user details
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

//    @GetMapping("/check-session")
//    public ResponseEntity<?> checkSession() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            String username = authentication.getName();
//            Customer user = customerService.getCustomerByUsername(username)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            // Return user details
//            return ResponseEntity.ok(Map.of(
//                    "id", user.getCustomerId(),
//                    "username", user.getUsername(),
//                    "role", user.getRole()
//            ));
//        }
//        return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
//    }
}