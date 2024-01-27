package com.entities;

public class Customer {
    private int customerId;
    private String name;
    private String email;
    private String mobileNum;
    private String password;
    private String address;

    public Customer() {
    }

    public Customer(int customerId, String name, String email, String mobileNum, String password, String address) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.mobileNum = mobileNum;
        this.password = password;
        this.address = address;
    }

    public Customer(String name, String email, String mobileNum, String password, String address) {
        this.name = name;
        this.email = email;
        this.mobileNum = mobileNum;
        this.password = password;
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("Customer Info..%nCustomerId : %d%nName : %s%nEmail : %s%nMobile : %s%nAddress : %s%n ",
                customerId,name,email,mobileNum,address);
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
