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
                .csrf(customizer -> customizer.disable()) // Disable CSRF
                .cors(Customizer.withDefaults()) // Enable CORS
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/user/add-user","/admin/all-bookings","/admin/drivers-with-availability", "/user/login", "/admin/assigned-drivers","/admin/accept-booking/{bookingId}","/admin/drivers-with-cars","/admin/available-drivers-withoutCar","/admin/available-cars")
                        .permitAll() // Allow public access to these endpoints
                        .requestMatchers("/add-booking","/bookings/**")
                        .hasRole("CUSTOMER") // Restrict access to CUSTOMER role
                        .requestMatchers("/admin/**") // Allow access to all admin endpoints
                        .hasRole("ADMIN") // Restrict access to ADMIN role
                        .anyRequest()
                        .authenticated()) // Require authentication for all other endpoints
                .httpBasic(Customizer.withDefaults()) // Use HTTP Basic authentication
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12)); // Use BCrypt password encoder
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}