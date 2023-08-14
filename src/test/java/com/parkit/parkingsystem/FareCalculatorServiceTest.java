package com.parkit.parkingsystem;

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


public class FareCalculatorServiceTest {

    @Mock
    private static FareCalculatorService fareCalculatorService;








        private Ticket ticket;

        @BeforeAll
        private static void setUp() {
            fareCalculatorService = new FareCalculatorService();
        }

        @BeforeEach
        private void setUpPerTest() {
            ticket = new Ticket();
        }

        @Test
        public void calculateFareCar() {
            Date inTime = new Date();
            inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
            Date outTime = new Date();
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            fareCalculatorService.calculateFare(ticket);
            assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
        }

        @Test
        public void calculateFareBike() {
            Date inTime = new Date();
            inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
            Date outTime = new Date();
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            fareCalculatorService.calculateFare(ticket);
            assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
        }

        @Test
        public void calculateFareUnkownType() {
            Date inTime = new Date();
            inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
            Date outTime = new Date();
            ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
        }

        @Test
        public void calculateFareBikeWithFutureInTime() {
            Date inTime = new Date();
            inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
            Date outTime = new Date();
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
        }

        @Test
        public void calculateFareBikeWithLessThanOneHourParkingTime() {
            Date inTime = new Date();
            inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));//45 minutes parking time should give 3/4th parking fare
            Date outTime = new Date();
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            fareCalculatorService.calculateFare(ticket);
            assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
        }

        @Test
        public void calculateFareCarWithLessThanOneHourParkingTime() {
            Date inTime = new Date();
            inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));//45 minutes parking time should give 3/4th parking fare
            Date outTime = new Date();
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            fareCalculatorService.calculateFare(ticket);
            assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
        }

        @Test
        public void calculateFareCarWithMoreThanADayParkingTime() {
            Date inTime = new Date();
            inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));//24 hours parking time should give 24 * parking fare per hour
            Date outTime = new Date();
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            fareCalculatorService.calculateFare(ticket);
            assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());


        }

        @Test
        public void calculateFareCarWithLessThan30minutesParkingTime() {
            Date inTime = new Date();
            inTime.setTime((long) (System.currentTimeMillis() - (0.4 * 60 * 60 * 1000)));
            Date outTime = new Date();
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            fareCalculatorService.calculateFare(ticket);
            assertEquals(0, ticket.getPrice());

        }

        @Test
        public void calculateFareBikeWithLessThan30minutesParkingTime() {
            Date inTime = new Date();
            inTime.setTime((long) (System.currentTimeMillis() - (0.4 * 60 * 60 * 1000)));
            Date outTime = new Date();
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            fareCalculatorService.calculateFare(ticket);
            assertEquals(0, ticket.getPrice());

        }


        @Test
        //this test must call the calculateFare method with a ticket for a car and with the parameter to true, then check that the price calculated is 95% of the full price.
        //of the full fare. The duration of the ticket must be more than 30 minutes.
        public void calculateFareCarWithDiscount() {

            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - 2 * 60 * 60 * 1000)); // 2 hours ago
            ticket.setOutTime(new Date());
            ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
            boolean isRegularUser = true;

            //Calculate the discount
            fareCalculatorService.calculateFare(ticket, isRegularUser);
            double calculatedPrice = ticket.getPrice();

            // Apply the discount
            double expectedPrice = 0.95 * 2 * Fare.CAR_RATE_PER_HOUR; // 5% discount applied for 2 hours of parking
            assertEquals(expectedPrice, calculatedPrice);

        }

        //For bikes
        @Test
        public void calculateFareBikeWithDiscount() {

            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - 3 * 60 * 60 * 1000)); // 3 hrs ago
            ticket.setOutTime(new Date());
            ticket.setParkingSpot(new ParkingSpot(1, ParkingType.BIKE, false));
            boolean isRegularUser = true;

            //Calculate the discount
            fareCalculatorService.calculateFare(ticket, isRegularUser);
            double calculatedPrice = ticket.getPrice();

            // Apply the discount
            double expectedPrice = 0.95 * 3 * Fare.BIKE_RATE_PER_HOUR; // 5% discount applied for 3 hours of parking
            assertEquals(expectedPrice, calculatedPrice);
        }
}
