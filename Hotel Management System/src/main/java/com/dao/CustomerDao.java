package com.dao;

import com.entities.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerDao {
    private Connection con;

    public CustomerDao(Connection con) {
        this.con = con;
    }

    //method to get customer data
    public Customer getCustomer(String username, String password, String credentialType) {
        Customer customer = null;
        String query = "Select * from CustomerDetails where MobileNo=? and Password=?";

        if (credentialType.equals("email")){
            query = "Select * from CustomerDetails where Email=? and Password=?";
        }

        try {
            // database --> CustomerDetails
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs =  ps.executeQuery();

            if (rs.next()) {
                customer = new Customer();

                customer.setCustomerId(rs.getInt("CustomerId"));
                customer.setName(rs.getString("Name"));
                customer.setPassword(rs.getString("Password"));
                customer.setEmail(rs.getString("Email"));
                customer.setMobileNum(rs.getString("MobileNo"));
                customer.setAddress(rs.getString("Address"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customer;
    }

    // method to add customer
    public boolean addCustomer(Customer customer) {
        boolean isAdded = false;
        try {

            // Database --> CustomerDetails
            String query = "INSERT INTO CustomerDetails(Name, Email, MobileNo, Password, Address)" +
                    " VALUES (?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getMobileNum());
            ps.setString(4, customer.getPassword());
            ps.setString(5, customer.getAddress());

            int row = ps.executeUpdate();

            if (row > 0) {
                isAdded = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isAdded;
    }

    // To Delete Employee
    public boolean deleteCustomer(Customer customer) {
        boolean isDeleted = false;
        try {

            // Database --> CustomerDetails
            String query = "DELETE FROM CustomerDetails WHERE CustomerId = ?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, customer.getCustomerId());

            int row = ps.executeUpdate();

            if (row > 0) {
                isDeleted = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isDeleted;
    }

    // Validate Mobile Number
    public boolean validateMobile(String mobile){
        boolean isValid = false;

        String query = "SELECT * FROM CustomerDetails WHERE MobileNo = ?";

        try{
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,mobile);

            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                isValid = true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return isValid;
    }

    // method to change password
    public boolean changePassword(String password, String mobile){
        boolean hasUpdated = false;

        String query = "UPDATE CustomerDetails SET Password = ? WHERE MobileNo = ?";

        try{
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,password);
            ps.setString(2,mobile);

            int row = ps.executeUpdate();
            if (row > 0){
                hasUpdated = true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return hasUpdated;
    }

    public ObservableList<Customer> getCustomerList() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        Customer customer;
        try {
            // CustomerDetails --> database
            String query = "Select * from CustomerDetails";
            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs =  ps.executeQuery();

            while (rs.next()) {
                customer = new Customer();

                customer.setCustomerId(rs.getInt("CustomerId"));
                customer.setName(rs.getString("Name"));
                customer.setEmail(rs.getString("Email"));
                customer.setMobileNum(rs.getString("MobileNo"));
                customer.setAddress(rs.getString("Address"));

                customerList.add(customer);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customerList;
    }

    // get customer Details using mobile/email
    public Customer getCustomer(String credentialType, String columnValue) {
        Customer customer = null;
        String query = "";

        try {
            PreparedStatement ps = null;
            if (credentialType.equals("email")) {
                query = "Select * from CustomerDetails where Email = ?";
                ps = con.prepareStatement(query);
            } else if (credentialType.equals("mobNumber")){
                query = "Select * from CustomerDetails where MobileNo = ?";
                ps = con.prepareStatement(query);
            }
            System.out.println("Query : "+query);
            ps.setString(1, columnValue);

            ResultSet rs =  ps.executeQuery();

            if (rs.next()) {
                System.out.println("CustomerDetails Found...");
                customer = new Customer();

                customer.setCustomerId(rs.getInt("CustomerId"));
                customer.setName(rs.getString("Name"));
                customer.setEmail(rs.getString("Email"));
                customer.setMobileNum(rs.getString("MobileNo"));
                customer.setAddress(rs.getString("Address"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customer;
    }

    // to get customer info based on customerId in search box
    public Customer getCustomerInfoOnSearch(int customerId){
        Customer customer = null;
        try {
            // CustomerDetails --> database
            String query = "SELECT * FROM CustomerDetails WHERE CustomerId = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1,customerId);

            ResultSet rs =  ps.executeQuery();

            if (rs.next()) {
                customer = new Customer();

                customer.setCustomerId(rs.getInt("CustomerId"));
                customer.setName(rs.getString("Name"));
                customer.setEmail(rs.getString("Email"));
                customer.setMobileNum(rs.getString("MobileNo"));
                customer.setAddress(rs.getString("Address"));

                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customer;
    }

}
