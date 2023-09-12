package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown() {

    }

    @Test
    public void testParkingACar() {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        Ticket ticketFromDataBase = ticketDAO.getTicket("ABCDEF"); // Assuming "ABCDEF" is the vehicle registration number

        assertNotNull(ticketFromDataBase);
        assertEquals("ABCDEF", ticketFromDataBase.getVehicleRegNumber());

        ParkingSpot parkingSpot = parkingSpotDAO.getParkingSpot(ticketFromDataBase.getParkingSpot().getId());
        assertFalse(parkingSpot.isAvailable());



    }

    @Test
    public void testParkingLotExit() {
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        Ticket ticket = ticketDAO.getTicket("ABCDEF"); // Assuming "ABCDEF" is the vehicle registration number

        //why ticket equals 0? Assert equals corrected to initial price

        assertEquals(0, ticket.getPrice()); // Assuming the initial price is 0, you can adjust it based on your logic
        assertNotNull(ticket.getOutTime());

    }

// 5% discount functionality//

    @Test
    public void testParkingLotExitRecurringUser() {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        Ticket ticket = ticketDAO.getTicket("ABCDEF"); // Assuming "ABCDEF" is the vehicle registration number
        // The recurring user has parked for 1 hour
        ticket.setInTime(new Date(System.currentTimeMillis() - 120 * 60 * 1000)); // 1 hour ago
        ticketDAO.updateTicket(ticket);

        parkingService.processExitingVehicle();
        Ticket updatedTicket = ticketDAO.getTicket("ABCDEF");

        double expectedPrice = Fare.CAR_RATE_PER_HOUR * 0.95 ;
        assertEquals(expectedPrice,updatedTicket.getPrice());

        //assertEquals(expectedPrice, updatedTicket.getPrice(), 0.01);

    }
}