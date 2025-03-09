package com.CabService.CabService.config;

import com.CabService.CabService.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF (not needed for stateless APIs)
                .cors(Customizer.withDefaults()) // Enable CORS with default configuration
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (no authentication required)
                        .requestMatchers(
                                "/user/add-user",
                                "/customer/api/reviews",
                                "/admin/bill/update/{bookingId}",
                                "/customer/add-booking",
                                "/admin/price-config",
                                "/admin/bill/{bookingId}",
                                "/admin/all-bookings",
                                "/admin/drivers-with-availability",
                                "/user/login",
                                "/user/check-session",
                                "/user/get-all-customer",
                                "/admin/assigned-drivers",
                                "/admin/accept-booking/{bookingId}",
                                "/admin/drivers-with-cars",
                                "/admin/available-drivers-withoutCar",
                                "/admin/available-cars"
                        ).permitAll()

                        // Customer-specific endpoints
                        .requestMatchers("/add-booking", "/bookings/**")
                        .hasRole("CUSTOMER")

                        // Admin-specific endpoints
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        // All other endpoints require authentication
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless session
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12)); // Use BCrypt password encoder with strength 12
        provider.setUserDetailsService(userDetailsService); // Set custom UserDetailsService
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}