package com.dao;

import com.entities.Customer;
import com.entities.Rooms;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class RoomDao {
    private Connection con;

    public RoomDao(Connection con) {
        this.con = con;
    }

    // To Add a Room
    public boolean addRoom(Rooms room) {
        boolean isAdded = false;
        try {

            // Database --> Rooms
            String query = "INSERT INTO ROOMS VALUES (?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, room.getRoomNo());
            ps.setString(2, room.getRoomType());
            ps.setDouble(3, room.getPrice());
            ps.setString(4, room.getStatus());

            int row = ps.executeUpdate();

            if (row == 1) {
                isAdded = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isAdded;
    }

    //method to get all Rooms data
    public ObservableList<Rooms> getRoomList() {
        ObservableList<Rooms> roomList = FXCollections.observableArrayList();
        Rooms room = null;
        try {
            String query = "SELECT * FROM ROOMS";
            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs =  ps.executeQuery();

            while (rs.next()) {
                room = new Rooms();

                room.setRoomNo(rs.getString("RoomNo"));
                room.setRoomType(rs.getString("Room_Type"));
                room.setPrice(rs.getDouble("Price"));
                room.setStatus(rs.getString("Status"));

                roomList.add(room);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return roomList;
    }

    //method to get a Room data
    public Rooms getRoom(String roomNo) {
        Rooms room = null;
        try {
            // EmployeeDetails --> database
            String query = "SELECT * FROM ROOMS WHERE RoomNo = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, roomNo);

            ResultSet rs =  ps.executeQuery();

            if (rs.next()) {
                room = new Rooms();

                room.setRoomNo(rs.getString("RoomNo"));
                room.setRoomType(rs.getString("Room_Type"));
                room.setPrice(rs.getDouble("Price"));
                room.setStatus(rs.getString("Status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return room;
    }

    public ArrayList<String> getAvailableRoomList() {
        ArrayList<String> roomList = new ArrayList<>();
        try {
            // EmployeeDetails --> database
            String query = "SELECT * FROM ROOMS WHERE STATUS = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, "Available");

            ResultSet rs =  ps.executeQuery();

            while (rs.next()) {
                String roomNo = rs.getString("RoomNo");
                roomList.add(roomNo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return roomList;
    }

    public boolean updateRoomStatus(String roomNo,String status){
        boolean isUpdated = false;
        try {

            // Database --> Rooms
            String query = "UPDATE ROOMS SET STATUS = ? WHERE RoomNo = ?";
            System.out.println("Query : "+query);

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, status);
            ps.setString(2, roomNo);

            int updatedRowCount = ps.executeUpdate();

            if (updatedRowCount == 1) {
                isUpdated = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isUpdated;
    }

    public boolean deleteRoom(Rooms room) {
        boolean isDeleted = false;
        try {

            // Database --> CustomerDetails
            String query = "DELETE FROM ROOMS WHERE RoomNo = ?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, room.getRoomNo());

            int row = ps.executeUpdate();

            if (row > 0) {
                isDeleted = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isDeleted;
    }
}
