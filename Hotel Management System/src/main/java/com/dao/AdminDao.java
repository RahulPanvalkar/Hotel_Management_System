package com.dao;

import com.entities.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDao {
    private Connection con;

    public AdminDao(Connection con) {
        this.con = con;
    }

    //method to get admin data while logged-in
    public Admin getAdmin(int id, String password) {
        Admin admin = null;
        try {
            // AdminDetails --> database
            String query = "Select * from AdminDetails where Id=? and Password=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                admin = new Admin();

                admin.setId(rs.getInt("Id"));
                admin.setName(rs.getString("Name"));
                admin.setPassword(rs.getString("Password"));
                admin.setEmail(rs.getString("Email"));
                admin.setMobNumber(rs.getString("Contact"));
                admin.setAddress(rs.getString("Address"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return admin;
    }

    //method to get admin data while profile update
    public Admin getAdmin(int id) {
        Admin admin = null;
        try {
            // AdminDetails --> database
            String query = "SELECT * FROM AdminDetails WHERE Id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                admin = new Admin();

                admin.setId(rs.getInt("Id"));
                admin.setName(rs.getString("Name"));
                admin.setPassword(rs.getString("Password"));
                admin.setEmail(rs.getString("Email"));
                admin.setMobNumber(rs.getString("Contact"));
                admin.setAddress(rs.getString("Address"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return admin;
    }

    //method to update admin data using profile
    public boolean updateAdmin(Admin admin) {
        boolean isUpdated = false;

        try {
            String query = "UPDATE AdminDetails SET NAME=?, EMAIL=?, CONTACT=? WHERE ID=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, admin.getName());
            ps.setString(2, admin.getEmail());
            ps.setString(3, admin.getMobNumber());
            ps.setInt(4, admin.getId());

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated == 1) {
                isUpdated = true;
            }

            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isUpdated;
    }

    public boolean validateMobile(String mobileNo) {
        boolean isValid = false;

        String query = "SELECT * FROM AdminDetails WHERE Contact = ?";

        try{
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,mobileNo);

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

        String query = "UPDATE AdminDetails SET Password = ? WHERE Contact = ?";

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
}
