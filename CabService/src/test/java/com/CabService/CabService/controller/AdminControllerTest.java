package com.CabService.CabService.controller;

import com.CabService.CabService.dto.BillUpdateRequest;
import com.CabService.CabService.dto.DriverWithCarDetails;
import com.CabService.CabService.model.*;
import com.CabService.CabService.service.AdminService;
import com.CabService.CabService.service.BillService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private BillService billService;

    @MockBean
    private CustomerService customerService;

//For the authenticate as a admin method
    private String getAdminToken() throws Exception {
        String loginRequest = "{\"username\": \"admin\", \"password\": \"admin\"}";
        String token = mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return new ObjectMapper().readTree(token).get("token").asText();
    }

    @Test
    public void testGetAllBookings() throws Exception {
        Booking booking1 = new Booking();
        booking1.setBookingId(1);
        booking1.setStatus("Pending");

        Booking booking2 = new Booking();
        booking2.setBookingId(2);
        booking2.setStatus("Accepted");

        List<Booking> mockBookings = Arrays.asList(booking1, booking2);
        when(adminService.getAllBookings()).thenReturn(mockBookings);

        mockMvc.perform(get("/admin/all-bookings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].bookingId").value(1))
                .andExpect(jsonPath("$[0].status").value("Pending"))
                .andExpect(jsonPath("$[1].bookingId").value(2))
                .andExpect(jsonPath("$[1].status").value("Accepted"));
    }

    @Test
    public void testGetDriversWithAssignedCars() throws Exception {
        // Arrange: Create mock DriverWithCarDetails objects
        DriverWithCarDetails driver1 = new DriverWithCarDetails();
        driver1.setDriverId(1);
        driver1.setName("John Doe");
        driver1.setCarModel("Toyota Camry");

        DriverWithCarDetails driver2 = new DriverWithCarDetails();
        driver2.setDriverId(2);
        driver2.setName("Jane Smith");
        driver2.setCarModel("Honda Accord");

        List<DriverWithCarDetails> mockDrivers = Arrays.asList(driver1, driver2);

        // Mock the behavior of adminService.getDriversWithAssignedCars()
        when(adminService.getDriversWithAssignedCars()).thenReturn(mockDrivers);

        // Act and Assert: Simulate the GET request and validate the response
        mockMvc.perform(get("/admin/drivers-with-cars"))
                .andExpect(status().isOk()); // Check status code
    }

    @Test
    public void testGetDriversWithUpdatedAvailability() throws Exception {
        // Arrange: Create mock Driver objects
        Driver driver1 = new Driver();
        driver1.setDriverId(1);
        driver1.setName("John Doe");
        driver1.setAvailable(true);

        Driver driver2 = new Driver();
        driver2.setDriverId(2);
        driver2.setName("Jane Smith");
        driver2.setAvailable(false);

        List<Driver> mockDrivers = Arrays.asList(driver1, driver2);

        // Mock the behavior of adminService.getDriversWithUpdatedAvailability()
        when(adminService.getDriversWithUpdatedAvailability()).thenReturn(mockDrivers);

        // Act and Assert: Simulate the GET request and validate the response
        mockMvc.perform(get("/admin/drivers-with-availability"))
                .andExpect(status().isOk()); // Check status code
    }

    @Test
    public void testGetAvailableCars() throws Exception {
        // Arrange: Create mock Car objects
        Car car1 = new Car();
        car1.setCarId(1);
        car1.setModel("Toyota Camry");
        car1.setAvailable(true);

        Car car2 = new Car();
        car2.setCarId(2);
        car2.setModel("Honda Accord");
        car2.setAvailable(true);

        List<Car> mockCars = Arrays.asList(car1, car2);

        // Mock the behavior of adminService.getAvailableCars()
        when(adminService.getAvailableCars()).thenReturn(mockCars);

        // Act and Assert: Simulate the GET request and validate the response
        mockMvc.perform(get("/admin/available-cars"))
                .andExpect(status().isOk()); // Check status code
    }


    @Test
    public void testGetAvailableDrivers() throws Exception {
        // Arrange: Create mock DriverWithCarDetails objects
        DriverWithCarDetails driver1 = new DriverWithCarDetails();
        driver1.setDriverId(1);
        driver1.setName("John Doe");
        driver1.setCarModel("Toyota Camry");

        DriverWithCarDetails driver2 = new DriverWithCarDetails();
        driver2.setDriverId(2);
        driver2.setName("Jane Smith");
        driver2.setCarModel("Honda Accord");

        List<DriverWithCarDetails> mockDrivers = Arrays.asList(driver1, driver2);

        // Mock the behavior of adminService.getAvailableDriversWithCarDetails()
        when(adminService.getAvailableDriversWithCarDetails()).thenReturn(mockDrivers);

        // Get the admin token
        String token = getAdminToken();

        // Act and Assert: Simulate the GET request with the token and validate the response
        mockMvc.perform(get("/admin/available-drivers")
                        .header("Authorization", "Bearer " + token)) // Include the token
                .andExpect(status().isOk()); // Check status code
    }

    @Test
    public void testGetAvailableDriversWithoutCar() throws Exception {
        // Arrange: Create mock Driver objects
        Driver driver1 = new Driver();
        driver1.setDriverId(1);
        driver1.setName("John Doe");
        driver1.setAvailable(true);

        Driver driver2 = new Driver();
        driver2.setDriverId(2);
        driver2.setName("Jane Smith");
        driver2.setAvailable(true);

        List<Driver> mockDrivers = Arrays.asList(driver1, driver2);

        // Mock the behavior of adminService.getAvailableDrivers()
        when(adminService.getAvailableDrivers()).thenReturn(mockDrivers);

        // Act and Assert: Simulate the GET request and validate the response
        mockMvc.perform(get("/admin/available-drivers-withoutCar"))
                .andExpect(status().isOk()); // Check status code
    }

    @Test
    public void testAddDriver() throws Exception {
        // Arrange: Create a mock Driver object
        Driver driver = new Driver();
        driver.setDriverId(1);
        driver.setName("John Doe");
        driver.setLicenseNumber("DL12345");
        driver.setPhone("1234567890");

        // Mock the behavior of adminService.addDriver()
        when(adminService.addDriver(driver)).thenReturn("Driver added successfully");

        // Get the admin token
        String token = getAdminToken();

        // Act and Assert: Simulate the POST request with the token and validate the response
        mockMvc.perform(post("/admin/add-driver")
                        .header("Authorization", "Bearer " + token) // Include the token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(driver)))
                .andExpect(status().isOk()) // Check status code
                .andExpect(content().string("Driver added successfully")); // Check response body
    }

    @Test
    public void testAcceptBooking() throws Exception {
        // Arrange: Mock the behavior of adminService.acceptBooking()
        when(adminService.acceptBooking(1)).thenReturn("Booking accepted successfully");

        // Act and Assert: Simulate the PUT request and validate the response
        mockMvc.perform(put("/admin/accept-booking/1"))
                .andExpect(status().isOk()) // Check status code
                .andExpect(content().string("Booking accepted successfully")); // Check response body
    }

    @Test
    public void testAddCar() throws Exception {
        // Arrange: Create a mock Car object
        Car car = new Car();
        car.setCarId(1);
        car.setModel("Toyota Camry");
        car.setLicensePlate("ABC123");
        car.setAvailable(true);

        // Mock the behavior of adminService.addCar()
        when(adminService.addCar(car)).thenReturn("Car added successfully");

        // Get the admin token
        String token = getAdminToken();

        // Act and Assert: Simulate the POST request with the token and validate the response
        mockMvc.perform(post("/admin/add-car")
                        .header("Authorization", "Bearer " + token) // Include the token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().isOk()) // Check status code
                .andExpect(content().string("Car added successfully")); // Check response body
    }

    @Test
    public void testAssignCarToDriver() throws Exception {
        // Arrange: Mock the behavior of adminService.assignCarToDriver()
        when(adminService.assignCarToDriver(1, 1)).thenReturn("Car assigned to driver successfully");

        // Get the admin token
        String token = getAdminToken();

        // Act and Assert: Simulate the POST request with the token and validate the response
        mockMvc.perform(post("/admin/assign-car-to-driver")
                        .header("Authorization", "Bearer " + token) // Include the token
                        .param("driverId", "1")
                        .param("carId", "1"))
                .andExpect(status().isOk()) // Check status code
                .andExpect(content().string("Car assigned to driver successfully")); // Check response body
    }

    @Test
    public void testGetBillByBookingId() throws Exception {
        // Arrange: Create a mock Bill object
        Bill bill = new Bill();
        bill.setBillId(1);
        bill.setBaseFare(100.0);
        bill.setTotalAmount(120.0);

        // Mock the behavior of billService.getBillByBookingId()
        when(billService.getBillByBookingId(1)).thenReturn(bill);

        // Act and Assert: Simulate the GET request and validate the response
        mockMvc.perform(get("/admin/bill/1"))
                .andExpect(status().isOk()) // Check status code
                .andExpect(jsonPath("$.billId").value(1)) // Check bill ID
                .andExpect(jsonPath("$.baseFare").value(100.0)) // Check base fare
                .andExpect(jsonPath("$.totalAmount").value(120.0)); // Check total amount
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetPriceConfig() throws Exception {
        // Arrange: Create a mock PriceConfig object
        PriceConfig priceConfig = new PriceConfig();
        priceConfig.setBaseFare(100.0);
        priceConfig.setWaitingTimeCharge(10.0);
        priceConfig.setTaxRate(5.0);
        priceConfig.setDiscountRate(2.0);

        // Mock the behavior of customerService.getDefaultPriceConfig()
        when(customerService.getDefaultPriceConfig()).thenReturn(priceConfig);

        // Act and Assert: Simulate the GET request and validate the response
        mockMvc.perform(get("/admin/price-config"))
                .andExpect(status().isOk()) // Check status code
                .andExpect(jsonPath("$.baseFare").value(100.0)) // Check base fare
                .andExpect(jsonPath("$.waitingTimeCharge").value(10.0)) // Check waiting time charge
                .andExpect(jsonPath("$.taxRate").value(5.0)) // Check tax rate
                .andExpect(jsonPath("$.discountRate").value(2.0)); // Check discount rate
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUpdatePriceConfig() throws Exception {
        // Arrange: Create a mock PriceConfig object
        PriceConfig priceConfig = new PriceConfig();
        priceConfig.setBaseFare(150.0);
        priceConfig.setWaitingTimeCharge(15.0);
        priceConfig.setTaxRate(7.0);
        priceConfig.setDiscountRate(3.0);

        // Mock the behavior of customerService.updatePriceConfig()
        when(customerService.updatePriceConfig(priceConfig)).thenReturn("Price config updated successfully");

        // Act and Assert: Simulate the PUT request and validate the response
        mockMvc.perform(put("/admin/price-config")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(priceConfig)))
                .andExpect(status().isOk()) // Check status code
                .andExpect(content().string("Price config updated successfully")); // Check response body
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUpdateBill() throws Exception {
        // Arrange: Create a mock BillUpdateRequest object
        BillUpdateRequest request = new BillUpdateRequest();
        request.setBaseFare(100.0);
        request.setWaitingTimeCharge(10.0);
        request.setTaxes(5.0);
        request.setDiscount(2.0);
        request.setTotalAmount(113.0);

        // Mock the behavior of billService.updateBill()
        when(billService.updateBill(1, 100.0, 10.0, 5.0, 2.0, 113.0))
                .thenReturn("Bill updated successfully");

        // Act and Assert: Simulate the PUT request and validate the response
        mockMvc.perform(put("/admin/bill/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk()) // Check status code
                .andExpect(content().string("Bill updated successfully")); // Check response body
    }
}
