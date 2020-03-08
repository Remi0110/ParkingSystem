package com.parkit.parkingsystem.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null)) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		LocalDateTime dateArrival = ticket.getInTime();
		LocalDateTime dateDeparture = ticket.getOutTime();
		
		long difference = ChronoUnit.MINUTES.between(dateArrival, dateDeparture);
		double duration = ((double) difference / Fare.MINUTES_PER_HOUR);

		// Parking is free if user stay less than 30min
		if (duration < Fare.RATE_HALF_AN_HOUR) {
			duration = Fare.FREE;
		}

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
			// a discount is made if user is reccurent
			calculDiscount(ticket.getPrice(), ticket);
			break;
		}
		case BIKE: {
			ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
			calculDiscount(ticket.getPrice(), ticket);
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}

	public void calculDiscount(double price, Ticket ticket) {
		if (ticket.isAvailableDiscount()) {
			double discount = (price * 5) / 100;
			price = price - discount;
		}
		ticket.setPrice(price);
	}
	
}