package com.controller.customer;

import com.entities.Customer;
import com.hms.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerDashboardController implements Initializable {
    @FXML
    public Label greetLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SessionManager<Customer> custSession = SessionManager.getInstance();
        Customer customer = custSession.getCurrentUser();

        if (customer != null) {
            String fullName = customer.getName();
            int idx = fullName.trim().indexOf(" ");
            if (idx < 0) {
                greetLabel.setText("Hey " + fullName);
            } else {
                String fName = fullName.substring(0, idx);
                greetLabel.setText("Hey " + fName);
            }
        }
    }
}