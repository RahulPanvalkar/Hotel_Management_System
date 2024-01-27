package com.hms;

import com.entities.Admin;
import com.entities.Employee;
import com.entities.Customer;
import com.utility.HMS_Utility;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class UserBarController implements Initializable {

    @FXML
    private Text username;

    SessionManager session;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        session = SessionManager.getInstance();
        var user = session.getCurrentUser();

        if (user == null) {
            System.out.println("user is null");
            return;
        }

        String userClass = user instanceof Admin ? "Admin" : (user instanceof Employee ? "Employee" : "Customer");
        System.out.println("userClass : [" + userClass + "]");

        if (userClass.equalsIgnoreCase("Admin")) {
            Admin admin = (Admin) user;
            String fullName = admin.getName();
            username.setText(fullName);
        } else if (userClass.equalsIgnoreCase("Employee")) {
            Employee emp = (Employee) user;
            String fullName = emp.getName();
            username.setText(fullName);
        } else if (userClass.equalsIgnoreCase("Customer")) {
            Customer customer = (Customer) user;
            String fullName = customer.getName();
            username.setText(fullName);
        }
    }

    @FXML
    public void handleLogout(MouseEvent event) {
        System.out.println("Logout triggered!");
        HMS_Utility.logout(event, session);
    }

    @FXML
    private void handleViewProfile(MouseEvent event) {
        System.out.println("View Profile clicked!");
        HMS_Utility.loadNewStage("/com/hms/UserProfile.fxml", "User Profile");
    }

}
