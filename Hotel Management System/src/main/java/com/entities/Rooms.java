package com.entities;

public class Rooms {

    private String roomNo;
    private String roomType;
    private double price;

    private String status;

    public Rooms() {
    }

    public Rooms(String roomNo, String roomType, double price, String status) {
        this.roomNo = roomNo;
        this.roomType = roomType;
        this.price = price;
        this.status = status;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
