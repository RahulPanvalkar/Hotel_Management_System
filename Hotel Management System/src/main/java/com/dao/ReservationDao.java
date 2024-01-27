package com.dao;

import com.entities.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReservationDao {
    private Connection con;

    public ReservationDao(Connection con) {
        this.con = con;
    }

    //method to get Earning Info
    public ObservableList<Reservation> getAllReservations() {
        ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
        Reservation reservation = null;
        try {
            String query = "SELECT * FROM Reservation";
            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                reservation = new Reservation();

                reservation.setReservationId(rs.getString("ReservationId"));
                reservation.setCustomerId(rs.getInt("CustomerId"));
                reservation.setCustomerName(rs.getString("CustomerName"));
                reservation.setRoomNo(rs.getString("Room_No"));
                reservation.setRoomType(rs.getString("Room_Type"));
                reservation.setCheckInTime(rs.getTimestamp("CheckIn_Time").toLocalDateTime());
                reservation.setPricePerDay(rs.getDouble("Price_Per_Day"));
                reservation.setTotalPrice(rs.getDouble("Total_Price"));

                java.sql.Timestamp checkOutTime = rs.getTimestamp("CheckOut_Time");
                System.out.println("checkOutTime : " + checkOutTime);
                if (checkOutTime != null)
                    reservation.setCheckOutTime(checkOutTime.toLocalDateTime());
                else
                    reservation.setCheckOutTime(null);

                reservationList.add(reservation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reservationList;
    }

    // to get Reservation based on reservationId in search
    public Reservation getReservationOnSearch(String reservationId) {
        Reservation reservation = null;
        try {
            String query = "SELECT * FROM Reservation WHERE ReservationId = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, reservationId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                reservation = new Reservation();

                reservation.setReservationId(rs.getString("ReservationId"));
                reservation.setCustomerId(rs.getInt("CustomerId"));
                reservation.setCustomerName(rs.getString("CustomerName"));
                reservation.setRoomNo(rs.getString("Room_No"));
                reservation.setRoomType(rs.getString("Room_Type"));
                reservation.setCheckInTime(rs.getTimestamp("CheckIn_Time").toLocalDateTime());
                reservation.setPricePerDay(rs.getDouble("Price_Per_Day"));
                reservation.setTotalPrice(rs.getDouble("Total_Price"));

                java.sql.Timestamp checkOutTime = rs.getTimestamp("CheckOut_Time");
                System.out.println("checkOutTime : " + checkOutTime);
                if (checkOutTime != null)
                    reservation.setCheckOutTime(checkOutTime.toLocalDateTime());
                else
                    reservation.setCheckOutTime(null);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reservation;
    }

    public boolean addReservation(Reservation res) {
        System.out.println("Inside addReservation...");
        boolean isAdded = false;
        try {
            // Reservation --> database
            String query = "INSERT INTO Reservation(ReservationId,CustomerId,CustomerName,Room_No,Room_Type,CheckIn_Time,Price_Per_Day,GST)" +
                    "VALUES (?,?,?,?,?,now(),?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, res.getReservationId());
            ps.setInt(2, res.getCustomerId());
            ps.setString(3, res.getCustomerName());
            ps.setString(4, res.getRoomNo());
            ps.setString(5, res.getRoomType());
            ps.setDouble(6, res.getPricePerDay());
            ps.setDouble(7, 12);

            int noOfRowUpdated = ps.executeUpdate();
            if (noOfRowUpdated == 1) {
                System.out.println("Checked In details added in Reservation table..");
                isAdded = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isAdded;
    }

    public boolean checkResNo(String resNo) {
        boolean isPresent = false;
        try {
            // Reservation --> database
            String query = "SELECT * FROM Reservation WHERE ReservationId = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, resNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                isPresent = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPresent;
    }

    // to get reservation details for check-out
    public ObservableList<Reservation> getReservationData() {
        ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
        Reservation reservation = null;
        try {
            String query = "SELECT * FROM Reservation WHERE CheckOut_Time IS NULL";
            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                reservation = new Reservation();

                reservation.setReservationId(rs.getString("ReservationId"));
                reservation.setCustomerId(rs.getInt("CustomerId"));
                reservation.setCustomerName(rs.getString("CustomerName"));
                reservation.setRoomNo(rs.getString("Room_No"));
                reservation.setRoomType(rs.getString("Room_Type"));
                reservation.setCheckInTime(rs.getTimestamp("CheckIn_Time").toLocalDateTime());
                reservation.setPricePerDay(rs.getDouble("Price_Per_Day"));

                reservationList.add(reservation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reservationList;
    }

    // to get reservation details for check-out search
    public Reservation getReservationDataOnSearch(String roomNo) {
        Reservation reservation = null;
        try {
            String query = "SELECT * FROM Reservation WHERE Total_Price IS NULL and Room_No = ? ";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, roomNo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                reservation = new Reservation();

                reservation.setReservationId(rs.getString("ReservationId"));
                reservation.setCustomerId(rs.getInt("CustomerId"));
                reservation.setCustomerName(rs.getString("CustomerName"));
                reservation.setRoomNo(rs.getString("Room_No"));
                reservation.setRoomType(rs.getString("Room_Type"));
                reservation.setCheckInTime(rs.getTimestamp("CheckIn_Time").toLocalDateTime());
                reservation.setPricePerDay(rs.getDouble("Price_Per_Day"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reservation;
    }

    // update Reservation table on checkOut
    public boolean updateReservation(String reservationId, int totalDays, double totalPrice) {
        System.out.println("Inside addReservation...");
        boolean isUpdated = false;
        try {
            // Reservation --> database
            String query = "UPDATE Reservation SET CheckOut_Time = now() , Total_Days = ?, Total_Price = ? WHERE ReservationId = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, totalDays);
            ps.setDouble(2, totalPrice);
            ps.setString(3, reservationId);

            int noOfRowUpdated = ps.executeUpdate();

            if (noOfRowUpdated == 1) {
                System.out.println("Check-Out details added in Reservation table..");
                isUpdated = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isUpdated;
    }

    // to get reservation data for a customer
    public ObservableList<Reservation> getResvnDataForCustomer(int custId) {
        ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
        try {
            // EmployeeDetails --> database
            String query = "SELECT * FROM Reservation WHERE CustomerId = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, custId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Reservation reservation = new Reservation();

                reservation.setReservationId(rs.getString("ReservationId"));
                reservation.setCustomerId(rs.getInt("CustomerId"));
                reservation.setCustomerName(rs.getString("CustomerName"));
                reservation.setRoomNo(rs.getString("Room_No"));
                reservation.setRoomType(rs.getString("Room_Type"));
                reservation.setCheckInTime(rs.getTimestamp("CheckIn_Time").toLocalDateTime());
                reservation.setPricePerDay(rs.getDouble("Price_Per_Day"));
                reservation.setTotalPrice(rs.getDouble("Total_Price"));

                java.sql.Timestamp checkOutTime = rs.getTimestamp("CheckOut_Time");
                System.out.println("checkOutTime : " + checkOutTime);
                if (checkOutTime != null)
                    reservation.setCheckOutTime(checkOutTime.toLocalDateTime());
                else
                    reservation.setCheckOutTime(null);

                reservationList.add(reservation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reservationList;
    }

    // Add Reservation for checkIn by customer
    public boolean addResvnByCustomer(Reservation res) {
        System.out.println("Inside addReservation...");
        boolean isAdded = false;
        try {
            // Reservation --> database
            String query = "INSERT INTO Reservation" +
                    "(ReservationId,CustomerId,CustomerName,Room_No,Room_Type,CheckIn_Time,CheckOut_Time,Price_Per_Day,Total_Days,GST,Total_Price)" +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd, hh:mm a");

            LocalDateTime dt1 = LocalDateTime.parse(res.getCheckInTime(),formatter);
            LocalDateTime dt2 = LocalDateTime.parse(res.getCheckOutTime(),formatter);

            java.sql.Timestamp checkIn =  java.sql.Timestamp.valueOf(dt1);
            java.sql.Timestamp checkOut =  java.sql.Timestamp.valueOf(dt2);
            System.out.println("checkIn : "+checkIn+" & checkOut : "+checkOut);

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, res.getReservationId());
            ps.setInt(2, res.getCustomerId());
            ps.setString(3, res.getCustomerName());
            ps.setString(4, res.getRoomNo());
            ps.setString(5, res.getRoomType());
            ps.setTimestamp(6, checkIn);
            ps.setTimestamp(7, checkOut);
            ps.setDouble(8, res.getPricePerDay());
            ps.setInt(9, res.getTotalDays());
            ps.setDouble(10, 12);
            ps.setDouble(11,res.getTotalPrice());

            int noOfRowUpdated = ps.executeUpdate();
            if (noOfRowUpdated == 1) {
                System.out.println("Checked In details added in Reservation table..");
                isAdded = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isAdded;
    }

}
