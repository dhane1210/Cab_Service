package com.CabService.CabService.service;
import com.CabService.CabService.model.Customer;
import com.CabService.CabService.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Customer addUser(Customer customer) {
        customer.setPassword(encoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }

    public String verify(Customer customer) {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(customer.getUsername(), customer.getPassword()));

        if (authentication.isAuthenticated()) {
            String role = authentication.getAuthorities().iterator().next().getAuthority();

            if ("ROLE_ADMIN".equals(role)) {
                // Redirect to admin UI (client-side redirection using a special response)
                return "redirect:admin-ui";
            }

            // Generate JWT token with username and role
            return jwtService.generateToken(customer.getUsername(), role);  // Pass role as the second argument
        }

        return "Fail";
    }




//    // Authenticate admin
//    public String authenticate(String username, String password) {
//        Admin admin = adminRepository.findByUsernameAndPassword(username, password);
//        return (admin != null) ? "Login successful" : "Invalid credentials";
//    }
}
