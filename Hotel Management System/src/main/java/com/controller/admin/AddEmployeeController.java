package com.controller.admin;

import com.dao.DeptAndJobTitleDao;
import com.dao.EmployeeDao;
import com.entities.Employee;
import com.utility.ConnectionProvider;
import com.utility.HMS_Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddEmployeeController implements Initializable {

    @FXML
    private TextField fullName;
    @FXML
    private TextField email;
    @FXML
    private TextField mobileNumber;
    @FXML
    private TextField address;
    @FXML
    private ChoiceBox<String> dept;
    @FXML
    private ChoiceBox<String> jobTitle;
    @FXML
    private Label message;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDeptList();
        setJobTitleList();
    }

    private void setDeptList() {
        Connection con = ConnectionProvider.getConnection();
        DeptAndJobTitleDao deptDao = new DeptAndJobTitleDao(con);
        ArrayList<String> deptList = deptDao.getDeptList();
        System.out.println("DeptList : " + deptList);
        dept.setValue("Department");
        dept.getItems().setAll(deptList);
    }

    private void setJobTitleList() {
        Connection con = ConnectionProvider.getConnection();
        DeptAndJobTitleDao deptDao = new DeptAndJobTitleDao(con);
        ArrayList<String> jobTitleList = deptDao.getJobTitleList();
        System.out.println("JobTitleList : " + jobTitleList);
        jobTitle.setValue("Job Title");
        jobTitle.getItems().setAll(jobTitleList);
    }

    public void handleChoiceBoxSelection(ActionEvent event) {
        String deptValue = dept.getValue().trim();
        System.out.println("deptValue : "+deptValue);
        if (!deptValue.equalsIgnoreCase("Department")){
            Connection con = ConnectionProvider.getConnection();
            DeptAndJobTitleDao deptDao = new DeptAndJobTitleDao(con);
            ArrayList<String> jobTitleList = deptDao.getJobTitleListForDept(deptValue);
            System.out.println("JobTitleList : " + jobTitleList);
            jobTitle.setValue("Job Title");
            jobTitle.getItems().setAll(jobTitleList);
        }
    }

    public void handleAddButton() {

        String empName = fullName.getText();
        String empEmail = email.getText();
        String empMobile = mobileNumber.getText();
        String empAddress = address.getText();
        String empDept = dept.getValue();
        String empJobTitle = jobTitle.getValue();

        if (empName.isEmpty() || empEmail.isEmpty()  || empMobile.isEmpty() || empAddress.isEmpty()
                || empDept.equalsIgnoreCase("Department") || empJobTitle.equalsIgnoreCase("Job Title")){
            message.setText("Field Cannot be empty!");
            return;
        }

        if (!HMS_Utility.emailValidation(empEmail) ){
            message.setText("Invalid Email Id!");
            return;
        }

        if (!HMS_Utility.mobileNumberValidation(empMobile) ){
            message.setText("Invalid Mobile Number!");
            return;
        }

        Employee employee = new Employee(empName, empEmail, empMobile,empAddress, empDept, empJobTitle);

        Connection con = ConnectionProvider.getConnection();
        EmployeeDao employeeDao = new EmployeeDao(con);

        boolean isAdded = employeeDao.addEmployee(employee);
        if (isAdded){
            message.setText("Registration Successful!");
            fullName.setText("");
            email.setText("");
            dept.setValue("Department");
            jobTitle.setValue("Job Title");
            address.setText("");
            mobileNumber.setText("");
        }else {
            message.setText("Registration Failed!");
        }
    }

    public void handleKeyTyped(KeyEvent event) {
        TextField textField = (TextField) event.getSource();

        if (textField.equals(fullName)) {
            int fullNameMaxLength = 50;
            HMS_Utility.limitTextFieldLength(fullName, fullNameMaxLength);
        } else if (textField.equals(email)) {
            int emailMaxLength = 30;
            HMS_Utility.limitTextFieldLength(email, emailMaxLength);
        } else if (textField.equals(mobileNumber)) {
            int mobileNumberMaxLength = 10;
            HMS_Utility.limitTextFieldLength(mobileNumber, mobileNumberMaxLength);

        } else if (textField.equals(address)) {
            int addressMaxLength = 100;
            HMS_Utility.limitTextFieldLength(address, addressMaxLength);
        } else {
            throw new IllegalStateException("Unexpected value: " + textField);
        }
    }

    public void handleClearButton(ActionEvent event) {
        fullName.setText("");
        email.setText("");
        dept.setValue("Department");
        address.setText("");
        mobileNumber.setText("");
        message.setText("Add Employee");
        setJobTitleList();
    }

}


