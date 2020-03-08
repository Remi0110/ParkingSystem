package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

public class FareCalculatorServiceTest {

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
    public void calculateFareCar(){
    	LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault()).minusHours(1);
//        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
    	LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareBike(){
    	LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault()).minusHours(1);
    	LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareUnkownType(){
    	LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault()).minusHours(1);
    	LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }


    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
      	LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault()).minusMinutes(45); //45 minutes parking time should give 3/4th parking fare
    	LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
    	LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault()).minusMinutes(45); //45 minutes parking time should give 3/4th parking fare
    	LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
    	LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault()).minusDays(1); //24 hours parking time should give 24 * parking fare per hour
    	LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithHalfAnHourParkingTime(){
    	LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault()).minusMinutes(30); //30 minutes parking time should give 0.5 * parking fare per hour
    	LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (0.5 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }
    
    @Test
    public void calculateFareBikeWithHalfAnHourParkingTime(){
    	LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault()).minusMinutes(30); //30 minutes parking time should give 0.5 * parking fare per hour
    	LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (0.5 * Fare.BIKE_RATE_PER_HOUR) , ticket.getPrice());
    }
    
    @Test
    public void calculateFareCarWithLessThanAnHalfHourParkingTime(){
    	LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault()).minusMinutes(15); //less than 30 minutes parking time should give free parking fare
    	LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( Fare.FREE , ticket.getPrice());
    }
    
    @Test
    public void calculateFareCarWithReccurentUser(){
    	LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault()).minusHours(1); //reccurent car user park for 60 minutes should give discount fare
    	LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setAvailableDiscount(true);
        double discount = (Fare.CAR_RATE_PER_HOUR * 5)/100;
        fareCalculatorService.calculateFare(ticket);
        assertEquals((1.5-discount) , ticket.getPrice());
    }
    
    @Test
    public void calculateFareBikeWithReccurentUser(){
    	LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault()).minusHours(1); //reccurent bike user park for 60 minutes should give discount fare
    	LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setAvailableDiscount(true);
        double discount = (Fare.BIKE_RATE_PER_HOUR * 5)/100;
        fareCalculatorService.calculateFare(ticket);
        assertEquals((1-discount) , ticket.getPrice());
    }
    
    @Test
    public void calculateFareCarWithNonReccurentUser(){
    	LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault()).minusHours(1); //non reccurent car user park for 60 minutes should not give discount fare
    	LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setAvailableDiscount(false);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((1 * Fare.CAR_RATE_PER_HOUR)  , ticket.getPrice());
    }
    
    @Test
    public void calculateFareBikeWithNonReccurentUser(){
    	LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault()).minusHours(1); //non reccurent bike user park for 60 minutes should not give discount fare
    	LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setAvailableDiscount(false);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((1 * Fare.BIKE_RATE_PER_HOUR)  , ticket.getPrice());
    }
    
    @Test
    public void calculateFareCarWithLessThanAnHalfHourAndReccurentUser(){
    	LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault()).minusMinutes(15); //reccurent car user park less than 30 minutes should give free parking fare
    	LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setAvailableDiscount(true);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( Fare.FREE , ticket.getPrice());
    }
    
    @Test
    public void calculateFareCarWithLessThanAnHalfHourAndNonReccurentUser(){
    	LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault()).minusMinutes(15); //non reccurent car user park less than 30 minutes should give free parking fare
    	LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setAvailableDiscount(false);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( Fare.FREE , ticket.getPrice());
    }
    
    @Test
    public void calculateFareBikeWithLessThanAnHalfHourAndReccurentUser(){
    	LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault()).minusMinutes(15); //reccurent bike user park less than 30 minutes should give free parking fare
    	LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setAvailableDiscount(true);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( Fare.FREE , ticket.getPrice());
    }
    
    @Test
    public void calculateFareBikeWithLessThanAnHalfHourAndNonReccurentUser(){
    	LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault()).minusMinutes(15); //non reccurent bike user park less than 30 minutes should give free parking fare
    	LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setAvailableDiscount(false);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( Fare.FREE , ticket.getPrice());
    }
    
    
}
