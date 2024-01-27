package com.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reservation {
    private String reservationId;
    private int customerId;
    private String customerName;
    private String roomNo;
    private String roomType;
    private String checkInTime;
    private String checkOutTime;
    private double pricePerDay;

    private int totalDays;
    private double gst;
    private double totalPrice;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy, hh:mm a");

    public Reservation() {
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public Reservation(String reservationId, int customerId, String customerName, String roomNo, String roomType, String checkInTime, String checkOutTime, double pricePerDay, int totalDays, double gst, double totalPrice) {
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.roomNo = roomNo;
        this.roomType = roomType;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.pricePerDay = pricePerDay;
        this.totalDays = totalDays;
        this.gst = gst;
        this.totalPrice = totalPrice;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime.format(formatter);
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalDateTime checkOutTime) {
        if (checkOutTime != null) {
            this.checkOutTime = checkOutTime.format(formatter);
        }
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public double getGst() {
        return gst;
    }

    public void setGst(double gst) {
        this.gst = gst;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
