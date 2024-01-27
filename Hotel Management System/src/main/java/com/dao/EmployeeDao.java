package com.dao;

import com.entities.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EmployeeDao {
    private Connection con;

    public EmployeeDao(Connection con) {
        this.con = con;
    }

    //method to get Employee data
    public Employee getEmployee(int id, String password) {
        Employee employee = null;
        try {
            // EmployeeDetails --> database
            String query = "Select * from EmployeeDetails where EmpId=? and Password=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ps.setString(2, password);

            ResultSet rs =  ps.executeQuery();

            if (rs.next()) {
                employee = new Employee();

                employee.setEmpId(rs.getInt("EmpId"));
                employee.setName(rs.getString("Name"));
                employee.setPassword(rs.getString("Password"));
                employee.setEmail(rs.getString("Email"));
                employee.setMobNumber(rs.getString("Contact"));
                employee.setAddress(rs.getString("Address"));
                employee.setDepartment(rs.getString("Department"));
                employee.setJobTitle(rs.getString("Job_Title"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return employee;
    }

    // To Add Employees
    public boolean addEmployee(Employee employee) {
        boolean isAdded = false;
        try {

            // Database --> EmployeeDetails
            String query = "INSERT INTO EmployeeDetails(Password, Name, Email, Contact, Address, Department, Job_Title)" +
                    " VALUES (?,?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, "hms@1234");
            ps.setString(2, employee.getName());
            ps.setString(3, employee.getEmail());
            ps.setString(4, employee.getMobNumber());
            ps.setString(5, employee.getAddress());
            ps.setString(6, employee.getDepartment());
            ps.setString(7, employee.getJobTitle());

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
    public boolean deleteEmployee(Employee employee) {
        boolean isDeleted = false;
        try {

            // Database --> EmployeeDetails
            String query = "DELETE FROM EmployeeDetails WHERE EmpId = ?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, employee.getEmpId());

            int row = ps.executeUpdate();

            if (row > 0) {
                isDeleted = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isDeleted;
    }

    //method to get all Employee data
    public ObservableList<Employee> getEmployeeList() {
        //ArrayList<Employee> empList = new ArrayList<>();
        ObservableList<Employee> empList = FXCollections.observableArrayList();
        Employee employee = null;
        try {
            // EmployeeDetails --> database
            String query = "Select * from EmployeeDetails";
            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs =  ps.executeQuery();

            while (rs.next()) {
                employee = new Employee();

                employee.setEmpId(rs.getInt("EmpId"));
                employee.setName(rs.getString("Name"));
                employee.setPassword(rs.getString("Password"));
                employee.setEmail(rs.getString("Email"));
                employee.setMobNumber(rs.getString("Contact"));
                employee.setAddress(rs.getString("Address"));
                employee.setDepartment(rs.getString("Department"));
                employee.setJobTitle(rs.getString("Job_Title"));

                empList.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return empList;
    }

    //method to get employee data while profile update
    public Employee getEmployee(int id) {
        Employee employee = null;
        try {
            // EmployeeDetails --> database
            String query = "SELECT * FROM EmployeeDetails WHERE EmpId=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                employee = new Employee();

                employee.setEmpId(rs.getInt("EmpId"));
                employee.setName(rs.getString("Name"));
                employee.setPassword(rs.getString("Password"));
                employee.setEmail(rs.getString("Email"));
                employee.setMobNumber(rs.getString("Contact"));
                employee.setAddress(rs.getString("Address"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return employee;
    }

    //method to update admin data using profile
    public boolean updateEmployee(Employee employee) {
        boolean isUpdated = false;

        try {
            String query = "UPDATE EmployeeDetails SET NAME=?, EMAIL=?, CONTACT=? WHERE EMPID=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getEmail());
            ps.setString(3, employee.getMobNumber());
            ps.setInt(4, employee.getEmpId());

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

    // Method to validate mobile number while Forget Password
    public boolean validateMobile(String mobileNo) {
        boolean isValid = false;

        String query = "SELECT * FROM EmployeeDetails WHERE Contact = ?";

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

        String query = "UPDATE EmployeeDetails SET Password = ? WHERE Contact = ?";

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

    public Employee getEmployeeInfoOnSearch(int employeeId) {
        Employee employee = null;
        try {
            // EmployeeDetails --> database
            String query = "SELECT * FROM EmployeeDetails WHERE EmpId = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1,employeeId);

            ResultSet rs =  ps.executeQuery();

            if (rs.next()) {
                employee = new Employee();

                employee.setEmpId(rs.getInt("EmpId"));
                employee.setName(rs.getString("Name"));
                employee.setPassword(rs.getString("Password"));
                employee.setEmail(rs.getString("Email"));
                employee.setMobNumber(rs.getString("Contact"));
                employee.setAddress(rs.getString("Address"));
                employee.setDepartment(rs.getString("Department"));
                employee.setJobTitle(rs.getString("Job_Title"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return employee;
    }

}
