package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;


public class TicketDAOTest {
	
	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static TicketDAO ticketDAO;
	
	
	 @BeforeAll
	    public static void setUpClass() throws Exception {
		 ticketDAO = new TicketDAO();
		 ticketDAO.dataBaseConfig = dataBaseTestConfig;		
	    }
	 
	 
	  @AfterAll
	    public static void tearDownClass() {

		  
	  }

	  @Test
	   public void testSaveTicket() throws SQLException {
		  	Ticket ticket = new Ticket();
		  	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);   
		  	ticket.setParkingSpot(parkingSpot);
		  	ticket.setVehicleRegNumber("ABCDEF");
		  	ticket.setPrice(Fare.CAR_RATE_PER_HOUR);
		  	LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault());
		  	ticket.setInTime(inTime);
		  	ticket.setAvailableDiscount(Boolean.TRUE);
		  	
	        boolean isFalse = ticketDAO.saveTicket(ticket);
	        assertEquals(isFalse, Boolean.FALSE);
	        
	    }
	  
	  @Test
	   public void testFailSaveTicketWithANullTicket() throws SQLException {
		  Ticket ticket = null;	  
		  assertThrows(
				  NullPointerException.class,
		            ()->ticketDAO.saveTicket(ticket));		  	
	    }
	  	  
	  
	  @Test
	   public void testGetTicket() throws Exception {	     
		   String vehicleRegNumber = "ABCDEF";
		   Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
		  
		   assertNotNull(ticket.getId());
		   assertNotNull(ticket.getInTime());
		   assertNotNull(ticket.getParkingSpot());
		   assertNotNull(ticket.getVehicleRegNumber());
		   assertNotNull(ticket.isAvailableDiscount());
	    }
	  
	  @Test
	   public void testFailGetTicketWithAnEmptyVehicleRegNumber() throws SQLException, IOException{
		   String vehicleRegNumber = "";		  
		assertThrows(
				  Exception.class,
		            ()->ticketDAO.getTicket(vehicleRegNumber));
		      		  	
	    }
	  
	  @Test
	   public void testUpdateTicket() throws SQLException, ClassNotFoundException, IOException {
		   Ticket ticket = new Ticket();
		   ticket.setPrice(Fare.CAR_RATE_PER_HOUR);
           LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());
		   ticket.setOutTime(outTime);
		   boolean isTrue = ticketDAO.updateTicket(ticket);
		   assertEquals(isTrue, Boolean.TRUE);
		   
	    }
	  
	  @Test
	   public void isReccurentUser() throws SQLException, ClassNotFoundException, IOException {
		   Ticket ticket = new Ticket();
		   ticket.setVehicleRegNumber("ABCDEF");
		   boolean isTrue = ticketDAO.isReccurentUser(ticket.getVehicleRegNumber());
		   assertTrue(isTrue);
		   
	    }
	  
	  
		            
		   

}
