package com.entities;

public class Employee {
    private int empId;
    private String password;
    private String name;
    private String email;
    private String mobNumber;
    private String address;
    private String department;
    private String jobTitle;

    public Employee() {

    }

    public Employee(int empId, String name, String email, String mobNumber) {
        this.empId = empId;
        this.name = name;
        this.email = email;
        this.mobNumber = mobNumber;
    }

    public Employee(String name, String email, String mobNumber, String address, String department, String jobTitle) {
        this.name = name;
        this.email = email;
        this.mobNumber = mobNumber;
        this.address = address;
        this.department = department;
        this.jobTitle = jobTitle;
    }

    public Employee(int empId, String password, String name, String email, String mobNumber,
                    String address, String department, String jobTitle) {
        this.empId = empId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.mobNumber = mobNumber;
        this.address = address;
        this.department = department;
        this.jobTitle = jobTitle;
    }

    @Override
    public String toString() {
        return String.format("Employee Info..%nEmpId : %d%nName : %s%nEmail : %s%nMobile : %s%nAddress : %s%nDept : %s%nJob : %s%n ",
                empId,name,email,mobNumber,address,department,jobTitle);
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
