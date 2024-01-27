package com.hms;

import com.utility.HMS_Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HomeController {

    public void processAdminLoginPressed(ActionEvent event) {
        SessionManager session = SessionManager.getInstance();
        session.setCurrentUserType("Admin");
        HMS_Utility.loadScene(event, "/Admin/AdminLogin.fxml", "Admin Login");

    }

    @FXML
    void processEmployeeLoginPressed(ActionEvent event) {
        SessionManager session = SessionManager.getInstance();
        session.setCurrentUserType("Employee");
        HMS_Utility.loadScene(event, "/Employee/EmployeeLogin.fxml", "Employee Login");

    }

    @FXML
    void processCustomerLoginPressed(ActionEvent event) {
        SessionManager session = SessionManager.getInstance();
        session.setCurrentUserType("Customer");
        HMS_Utility.loadScene(event, "/Customer/CustomerLogin.fxml", "Customer Login");

    }

}
