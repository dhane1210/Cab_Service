package com.CabService.CabService.controller;

import com.CabService.CabService.model.Customer;
import com.CabService.CabService.service.CustomerService;
import com.CabService.CabService.service.JWTService;
import com.CabService.CabService.service.UserAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private UserAuthService userAuthService;

    @Test
    public void testAddCustomer_Success() throws Exception {
        // Arrange: Create a mock Customer object
        Customer customer = new Customer();
        customer.setUsername("testuser");
        customer.setPassword("password");
        customer.setRole("CUSTOMER");

        // Mock the behavior of userAuthService.addUser()
        when(userAuthService.addUser(customer)).thenReturn(customer);

        // Mock the behavior of jwtService.generateToken()
        when(jwtService.generateToken("testuser", "CUSTOMER")).thenReturn("mockToken");

        // Act and Assert: Simulate the POST request and validate the response
        mockMvc.perform(post("/user/add-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customer)))
                .andExpect(status().isOk()) // Check status code
                .andExpect(jsonPath("$.token").value("mockToken")) // Check token
                .andExpect(jsonPath("$.username").value("testuser")) // Check username
                .andExpect(jsonPath("$.role").value("CUSTOMER")); // Check role
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        // Arrange: Create mock customers
        Customer customer1 = new Customer();
        customer1.setCustomerId(1);
        customer1.setUsername("user1");

        Customer customer2 = new Customer();
        customer2.setCustomerId(2);
        customer2.setUsername("user2");

        List<Customer> mockCustomers = Arrays.asList(customer1, customer2);

        // Mock the behavior of customerService.getCustomer()
        when(customerService.getCustomer()).thenReturn(mockCustomers);

        // Act and Assert: Simulate the GET request and validate the response
        mockMvc.perform(get("/user/get-all-customer"))
                .andExpect(status().isOk()) // Check status code
                .andExpect(jsonPath("$[0].customerId").value(1)) // Check first customer's ID
                .andExpect(jsonPath("$[0].username").value("user1")) // Check first customer's username
                .andExpect(jsonPath("$[1].customerId").value(2)) // Check second customer's ID
                .andExpect(jsonPath("$[1].username").value("user2")); // Check second customer's username
    }

    @Test
    public void testLogin_Success() throws Exception {
        // Arrange: Create a mock Customer object
        Customer customer = new Customer();
        customer.setUsername("testuser");
        customer.setPassword("password");

        // Mock the behavior of userAuthService.verify()
        when(userAuthService.verify(customer)).thenReturn("mockToken");

        // Mock the behavior of customerService.getCustomerByUsername()
        Customer loggedInUser = new Customer();
        loggedInUser.setCustomerId(1);
        loggedInUser.setUsername("testuser");
        loggedInUser.setRole("CUSTOMER");
        when(customerService.getCustomerByUsername("testuser")).thenReturn(Optional.of(loggedInUser));

        // Mock the behavior of jwtService.generateToken()
        when(jwtService.generateToken("testuser", "CUSTOMER")).thenReturn("newMockToken");

        // Act and Assert: Simulate the POST request and validate the response
        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customer)))
                .andExpect(status().isOk()) // Check status code
                .andExpect(jsonPath("$.token").value("newMockToken")) // Check token
                .andExpect(jsonPath("$.username").value("testuser")) // Check username
                .andExpect(jsonPath("$.role").value("CUSTOMER")); // Check role
    }

    @Test
    public void testLogin_InvalidCredentials() throws Exception {
        // Arrange: Create a mock Customer object
        Customer customer = new Customer();
        customer.setUsername("testuser");
        customer.setPassword("wrongpassword");

        // Mock the behavior of userAuthService.verify()
        when(userAuthService.verify(customer)).thenReturn("Fail");

        // Act and Assert: Simulate the POST request and validate the error response
        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customer)))
                .andExpect(status().isUnauthorized()) // Check status code
                .andExpect(jsonPath("$.error").value("Invalid credentials")); // Check error message
    }

    @Test
    public void testCheckSession_Success() throws Exception {
        // Arrange: Mock the behavior of jwtService.extractUserName()
        when(jwtService.extractUserName("mockToken")).thenReturn("testuser");

        // Mock the behavior of customerService.getCustomerByUsername()
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setUsername("testuser");
        customer.setRole("CUSTOMER");
        when(customerService.getCustomerByUsername("testuser")).thenReturn(Optional.of(customer));

        // Act and Assert: Simulate the GET request and validate the response
        mockMvc.perform(get("/user/check-session")
                        .header("Authorization", "Bearer mockToken"))
                .andExpect(status().isOk()) // Check status code
                .andExpect(jsonPath("$.username").value("testuser")) // Check username
                .andExpect(jsonPath("$.role").value("CUSTOMER")); // Check role
    }
}