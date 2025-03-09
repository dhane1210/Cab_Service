package com.CabService.CabService.controller;


import com.CabService.CabService.dto.BookingRequest;
import com.CabService.CabService.model.Bill;
import com.CabService.CabService.model.Booking;
import com.CabService.CabService.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"}) // Mock an authenticated customer
    public void testAddBooking_Success() throws Exception {
        // Arrange: Create a mock BookingRequest object
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCustomerId(1);
        bookingRequest.setStartLocation("Location A");
        bookingRequest.setEndLocation("Location B");
        bookingRequest.setDistance(10.0);

        // Mock the behavior of customerService.addBooking()
        when(customerService.addBooking(bookingRequest)).thenReturn("Booking added successfully");

        // Act and Assert: Simulate the POST request and validate the response
        mockMvc.perform(post("/customer/add-booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookingRequest)))
                .andExpect(status().isOk()) // Check status code
                .andExpect(content().string("Booking added successfully")); // Check response body
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"}) // Mock an authenticated customer
    public void testAddBooking_Error() throws Exception {
        // Arrange: Create a mock BookingRequest object
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCustomerId(1);
        bookingRequest.setStartLocation("Location A");
        bookingRequest.setEndLocation("Location B");
        bookingRequest.setDistance(10.0);

        // Mock the behavior of customerService.addBooking() to throw an exception
        when(customerService.addBooking(bookingRequest)).thenThrow(new RuntimeException("Failed to add booking"));

        // Act and Assert: Simulate the POST request and validate the error response
        mockMvc.perform(post("/customer/add-booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookingRequest)))
                .andExpect(status().isInternalServerError()) // Check status code
                .andExpect(content().string("Failed to add booking")); // Check error message
    }


    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    public void testDeleteBooking_Success() throws Exception {
        // Arrange: Mock the behavior of customerService.deleteBooking()
        doNothing().when(customerService).deleteBooking(1);

        // Act and Assert: Simulate the DELETE request and validate the response
        mockMvc.perform(delete("/customer/delete-booking/1"))
                .andExpect(status().isOk()) // Check status code
                .andExpect(content().string("Booking deleted successfully")); // Check response body
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    public void testDeleteBooking_Error() throws Exception {
        // Arrange: Mock the behavior of customerService.deleteBooking() to throw an exception
        doThrow(new RuntimeException("Failed to delete booking")).when(customerService).deleteBooking(1);

        // Act and Assert: Simulate the DELETE request and validate the error response
        mockMvc.perform(delete("/customer/delete-booking/1"))
                .andExpect(status().isInternalServerError()) // Check status code
                .andExpect(content().string("Failed to delete booking")); // Check error message
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    public void testViewBookings() throws Exception {
        // Arrange: Create mock bookings
        Booking booking1 = new Booking();
        booking1.setBookingId(1);
        booking1.setStatus("Pending");

        Booking booking2 = new Booking();
        booking2.setBookingId(2);
        booking2.setStatus("Accepted");

        List<Booking> mockBookings = Arrays.asList(booking1, booking2);

        // Mock the behavior of customerService.getBookingsByCustomer()
        when(customerService.getBookingsByCustomer(1)).thenReturn(mockBookings);

        // Act and Assert: Simulate the GET request and validate the response
        mockMvc.perform(get("/customer/view-bookings/1"))
                .andExpect(status().isOk()) // Check status code
                .andExpect(jsonPath("$[0].bookingId").value(1)) // Check first booking's ID
                .andExpect(jsonPath("$[0].status").value("Pending")) // Check first booking's status
                .andExpect(jsonPath("$[1].bookingId").value(2)) // Check second booking's ID
                .andExpect(jsonPath("$[1].status").value("Accepted")); // Check second booking's status
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    public void testFinalizeBooking_Success() throws Exception {
        // Arrange: Mock the behavior of customerService.finalizeBooking()
        when(customerService.finalizeBooking(1)).thenReturn("Booking finalized successfully");

        // Act and Assert: Simulate the PUT request and validate the response
        mockMvc.perform(put("/customer/finalize-booking/1"))
                .andExpect(status().isOk()) // Check status code
                .andExpect(content().string("Booking finalized successfully")); // Check response body
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    public void testFinalizeBooking_Error() throws Exception {
        // Arrange: Mock the behavior of customerService.finalizeBooking() to throw an exception
        when(customerService.finalizeBooking(1)).thenThrow(new RuntimeException("Failed to finalize booking"));

        // Act and Assert: Simulate the PUT request and validate the error response
        mockMvc.perform(put("/customer/finalize-booking/1"))
                .andExpect(status().isInternalServerError()) // Check status code
                .andExpect(content().string("Failed to finalize booking")); // Check error message
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    public void testViewBill() throws Exception {
        // Arrange: Create a mock Bill object
        Bill bill = new Bill();
        bill.setBillId(1);
        bill.setBaseFare(100.0);
        bill.setTotalAmount(120.0);

        // Mock the behavior of customerService.getBill()
        when(customerService.getBill(1)).thenReturn(bill);

        // Act and Assert: Simulate the GET request and validate the response
        mockMvc.perform(get("/customer/view-bill/1"))
                .andExpect(status().isOk()) // Check status code
                .andExpect(jsonPath("$.billId").value(1)) // Check bill ID
                .andExpect(jsonPath("$.baseFare").value(100.0)) // Check base fare
                .andExpect(jsonPath("$.totalAmount").value(120.0)); // Check total amount
    }
}