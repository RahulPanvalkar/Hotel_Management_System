package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DeptAndJobTitleDao {
    private Connection con;

    public DeptAndJobTitleDao(Connection con) {
        this.con = con;
    }

    // get department list
    public ArrayList<String> getDeptList() {
        ArrayList<String> deptList = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT deptName FROM DeptAndJobTitle ORDER BY deptName ASC";
            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs =  ps.executeQuery();

            while (rs.next()) {
                String dept = rs.getString("deptName");
                deptList.add(dept);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deptList;
    }

    // get JobTitle List
    public ArrayList<String> getJobTitleList() {
        ArrayList<String> deptList = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT jobTitleName FROM DeptAndJobTitle ORDER BY jobTitleName ASC";
            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs =  ps.executeQuery();

            while (rs.next()) {
                String jobTitleName = rs.getString("jobTitleName");
                deptList.add(jobTitleName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deptList;
    }

    // get JobTitle List for dept
    public ArrayList<String> getJobTitleListForDept(String dept) {
        ArrayList<String> deptList = new ArrayList<>();
        try {
            String query = "SELECT jobTitleName FROM DeptAndJobTitle WHERE deptName = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,dept);

            ResultSet rs =  ps.executeQuery();

            while (rs.next()) {
                String jobTitleName = rs.getString("jobTitleName");
                deptList.add(jobTitleName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deptList;
    }
}
