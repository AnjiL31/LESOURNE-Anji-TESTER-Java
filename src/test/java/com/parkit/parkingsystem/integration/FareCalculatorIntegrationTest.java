package com.parkit.parkingsystem.integration;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class FareCalculatorIntegrationTest {
    private FareCalculatorService fareCalculatorService;

    @BeforeEach
    public void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }
    @Test
    public void testCalculateFareCarWithDiscount() {
        // Create a ticket for a car with a regular user discount
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - 2 * 60 * 60 * 1000)); // 2 hours ago
        ticket.setOutTime(new Date());
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));

        // calculate the discount?
        fareCalculatorService.calculateFare(ticket, true);

        // caclucate the price with 5% discount for 3 hours of parking?
        double expectedPrice = 0.95 * 2 * Fare.CAR_RATE_PER_HOUR;

        // Verify that the actual price matches the expected price
        assertEquals(expectedPrice, ticket.getPrice(),0.001);
    }

}

