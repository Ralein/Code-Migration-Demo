package com.automotive;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
public class VehicleDAO {

    public boolean addVehicle(Vehicle vehicle) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
            		"INSERT INTO VEHICLE (VEHICLE_ID, OWNER_NAME, MODEL_NAME, REG_NUMBER) VALUES (?,?,?,?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1,
                    vehicle.getVehicleId());

            ps.setString(2,
                    vehicle.getOwnerName());

            ps.setString(3,
                    vehicle.getModelName());

            ps.setString(4,
                    vehicle.getRegNumber());

            int rows =
                    ps.executeUpdate();

            return rows > 0;

        } catch(Exception e) {

            e.printStackTrace();
        }

        return false;
    }
    public List<Vehicle> getAllVehicles() {

        List<Vehicle> vehicleList = new ArrayList<Vehicle>();

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT * FROM VEHICLE ORDER BY VEHICLE_ID";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while(rs.next()) {

                Vehicle vehicle = new Vehicle();

                vehicle.setVehicleId(
                        rs.getInt("VEHICLE_ID"));

                vehicle.setOwnerName(
                        rs.getString("OWNER_NAME"));

                vehicle.setModelName(
                        rs.getString("MODEL_NAME"));

                vehicle.setRegNumber(
                        rs.getString("REG_NUMBER"));

                vehicleList.add(vehicle);
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

        return vehicleList;
    }
    public Vehicle searchVehicle(int vehicleId) {

        Vehicle vehicle = null;

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "SELECT * FROM VEHICLE WHERE VEHICLE_ID=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, vehicleId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                vehicle = new Vehicle();

                vehicle.setVehicleId(
                        rs.getInt(1));

                vehicle.setOwnerName(
                        rs.getString(2));

                vehicle.setModelName(
                        rs.getString(3));

                vehicle.setRegNumber(
                        rs.getString(4));
            }

            con.close();

        } catch(Exception e) {

            e.printStackTrace();
        }

        return vehicle;
    }
    public boolean deleteVehicle(int vehicleId) {

        boolean status = false;

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "DELETE FROM VEHICLE WHERE VEHICLE_ID=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, vehicleId);

            int rows = ps.executeUpdate();

            if(rows > 0) {
                status = true;
            }

            con.close();

        } catch(Exception e) {

            e.printStackTrace();
        }

        return status;
    }
    public Vehicle getVehicleById(int vehicleId) {

        Vehicle vehicle = null;

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "SELECT * FROM VEHICLE WHERE VEHICLE_ID=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, vehicleId);

            ResultSet rs =
                    ps.executeQuery();

            if(rs.next()) {

                vehicle = new Vehicle();

                vehicle.setVehicleId(rs.getInt(1));
                vehicle.setOwnerName(rs.getString(2));
                vehicle.setModelName(rs.getString(3));
                vehicle.setRegNumber(rs.getString(4));
            }

            con.close();

        } catch(Exception e) {

            e.printStackTrace();
        }

        return vehicle;
    }
    public boolean updateVehicle(
            Vehicle vehicle) {

        boolean status = false;

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "UPDATE VEHICLE " +
                    "SET OWNER_NAME=?, " +
                    "MODEL_NAME=?, " +
                    "REG_NUMBER=? " +
                    "WHERE VEHICLE_ID=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1,
                    vehicle.getOwnerName());

            ps.setString(2,
                    vehicle.getModelName());

            ps.setString(3,
                    vehicle.getRegNumber());

            ps.setInt(4,
                    vehicle.getVehicleId());

            int rows =
                    ps.executeUpdate();

            status = rows > 0;

            con.close();

        } catch(Exception e) {

            e.printStackTrace();
        }

        return status;
    }
}