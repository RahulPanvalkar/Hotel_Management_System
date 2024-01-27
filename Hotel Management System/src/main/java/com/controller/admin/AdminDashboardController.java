package com.controller.admin;

import com.entities.Admin;
import com.hms.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    @FXML
    private Label greetLabel;

    SessionManager<Admin> adminSession;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        adminSession = SessionManager.getInstance();
        Admin admin = adminSession.getCurrentUser();

        if (admin != null) {
            String fullName = admin.getName();
            int idx = fullName.trim().indexOf(" ");
            if (idx < 0){
                greetLabel.setText("Hey " + fullName);
            }else {
                String fName = fullName.substring(0, idx);
                greetLabel.setText("Hey " + fName);
            }
        }
    }

}

