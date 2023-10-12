package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ParkingSpotDAO {
    private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    public int getNextAvailableSlot(ParkingType parkingType){
        Connection con = null;
        int result=-1;
        try {
            con = dataBaseConfig.getConnection();
            System.out.println("con = " + con);

            PreparedStatement ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
            ps.setString(1, parkingType.toString());
            ResultSet rs = ps.executeQuery();
            System.out.println("rs.getFetchSize() = " + rs.getFetchSize());
            if(rs.next()){
                result = rs.getInt(1);;


            }

            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
        }
        return result;
    }

    public boolean updateParking(ParkingSpot parkingSpot){
        //update the availability for that parking slot
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
            ps.setBoolean(1, parkingSpot.isAvailable());
            ps.setInt(2, parkingSpot.getId());
            int updateRowCount = ps.executeUpdate();
            dataBaseConfig.closePreparedStatement(ps);
            return (updateRowCount == 1);
        }catch (Exception ex){
            logger.error("Error updating parking info",ex);
            return false;
        }finally {
            dataBaseConfig.closeConnection(con);
        }

    }
    public ParkingSpot getParkingSpot(int parkingNumber){
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_PARKING_SPOT);//request
            ps.setInt(1,parkingNumber);
            ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            ParkingSpot parkingSpot = new ParkingSpot();

            parkingSpot.setId(rs.getInt("PARKING_NUMBER"));
            parkingSpot.setParkingType(ParkingType.valueOf(rs.getString("type")));
            parkingSpot.setAvailable(rs.getBoolean("available"));



            return parkingSpot;
        } else {
            return null; //Cannot find parking spot
        }

        }catch (Exception ex) {
            logger.error("Error retrieving Parking Number");
            return null;
        }finally {
            dataBaseConfig.closeConnection(con);
        }

    }
    public boolean insertParkingSpot(ParkingSpot parkingSpot) {
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_PARKING_SPOT);

            // Set the values for the prepared statement
            ps.setInt(1, parkingSpot.getId());
            ps.setString(2, parkingSpot.getParkingType().toString());
            ps.setBoolean(3, parkingSpot.isAvailable());

            // Execute the insert query
            int rowsInserted = ps.executeUpdate();

            // Check if the insert was successful (1 row should be inserted)
            return rowsInserted == 1;
        } catch (Exception ex) {
            logger.error("Error inserting parking spot", ex);
        } finally {
            dataBaseConfig.closeConnection(con);
        }
        return false; // Return false if the insertion fails



    }
}
