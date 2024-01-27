package com.hms;

import com.utility.HMS_Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.css.PseudoClass;

public class NavbarController {

    @FXML
    private Button adminHome;

    @FXML
    private Button addEmployee;

    @FXML
    private Button employeeInfo;

    @FXML
    private Button customerInfo;

    @FXML
    private Button earningLog;


    public void handleAdminHome(ActionEvent event) {
        System.out.println("inside handleAdminHome..");
        HMS_Utility.loadScene(event, "/Admin/AdminDashboard.fxml", "Admin Dashboard");
    }

    public void handleAddEmployee(ActionEvent event) {
        addEmployee.getStyleClass().add("clicked-button");
        HMS_Utility.loadScene(event, "/Admin/AddEmployee.fxml", "Add Employee");
    }

    public void handleEmployeeInfo(ActionEvent event) {
        employeeInfo.getStyleClass().add("clicked-button");
        HMS_Utility.loadScene(event, "/Admin/EmployeeInfo.fxml", "Employee Information");
    }

    public void handleCustomerInfo(ActionEvent event) {
        customerInfo.getStyleClass().add("clicked-button");
        HMS_Utility.loadScene(event, "/Admin/CustomerInfo.fxml", "Customer Information");
    }

    public void handleEarningLog(ActionEvent event) {
        earningLog.getStyleClass().add("clicked-button");
        HMS_Utility.loadScene(event, "/Admin/EarningLog.fxml", "Earning Log");
    }

    public void handleEmployeeHome(ActionEvent event) {
        HMS_Utility.loadScene(event, "/Employee/EmployeeDashboard.fxml", "Employee Dashboard");
    }

    public void handleRoomCheck(ActionEvent event) {
        HMS_Utility.loadScene(event, "/Employee/RoomCheck.fxml", "Room Check");
    }

    public void handleEmployeeCheckIn(ActionEvent event) {
        HMS_Utility.loadScene(event, "/Employee/CheckIn.fxml", "Check In");
    }

    public void handleEmployeeCheckOut(ActionEvent event) {
        HMS_Utility.loadScene(event, "/Employee/CheckOut.fxml", "Check Out");
    }

    public void handleEmployeeCheckHistory(ActionEvent event) {
        HMS_Utility.loadScene(event, "/Employee/CheckHistory.fxml", "Reservation History");
    }

    public void handleCustomerHome(ActionEvent event) {
        HMS_Utility.loadScene(event, "/Customer/CustomerDashboard.fxml", "Customer Dashboard");
    }

    public void handleRoomDetails(ActionEvent event) {
        HMS_Utility.loadScene(event, "/Customer/RoomDetails.fxml", "Room Details");
    }

    public void handleCustomerCheckIn(ActionEvent event) {
        HMS_Utility.loadScene(event, "/Customer/CheckIn.fxml", "Check In");
    }

    public void handleCustomerCheckHistory(ActionEvent event) {
        HMS_Utility.loadScene(event, "/Customer/CheckHistory.fxml", "Reservation History");
    }
}
