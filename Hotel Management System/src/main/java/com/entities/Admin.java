package com.entities;

public class Admin {
    private int id;
    private String password;
    private String name;
    private String email;
    private String mobNumber;
    private String address;

    public Admin() {

    }

    public Admin(int id, String name, String email, String mobNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobNumber = mobNumber;
    }

    public Admin(int id, String password, String name, String email, String mobNumber, String address) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.mobNumber = mobNumber;
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("Admin Info..%nId : %s%nName : %s%nEmail : %s%nMobile : %s%nAddress : %s%n ",
                id,name,email,mobNumber,address);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobNumber() {
        return mobNumber;
    }

    public void setMobNumber(String mobNumber) {
        this.mobNumber = mobNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
