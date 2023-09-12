package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
    public void calculateFare(Ticket ticket) {
        this.calculateFare(ticket,false);
    }

    public void calculateFare(Ticket ticket, boolean discount) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }


        //Calculate time in hours
        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();

        System.out.println("inHour = " + inHour);
        System.out.println("outHour = " + outHour);

        //The time is now in hours instead of milliseconds
        double duration = outHour - inHour; // verify that its 1hour
        double durationInHour = duration / (3600000d);

        // If the client parks less than 30 minutes. STEP 3
        if (durationInHour <= 0.5) {
            ticket.setPrice(Fare.MINIMUM_PRICE);

            return;
        }
        switch (ticket.getParkingSpot().

                getParkingType()) {
            case CAR: {
                ticket.setPrice(durationInHour * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(durationInHour * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }
        if (discount) {
            ticket.setPrice(0.95 * ticket.getPrice());
        }


    }


}
