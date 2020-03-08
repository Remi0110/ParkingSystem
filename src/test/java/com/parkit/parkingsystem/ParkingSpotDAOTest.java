package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;

public class ParkingSpotDAOTest {
	
	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	
	
	 @BeforeAll
	    public static void setUpClass() throws Exception {
		 parkingSpotDAO = new ParkingSpotDAO();
		 parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;		
		 
	    }
	 
	 
	  @AfterAll
	    public static void tearDownClass() {

		  
	  }

	  
	  @Test
	   public void testUpdateParking() throws SQLException {
		  	
		  	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);  			  	
	        boolean isTrue = parkingSpotDAO.updateParking(parkingSpot);
	        assertEquals(isTrue, Boolean.TRUE);
	        
	    }
		   

}
