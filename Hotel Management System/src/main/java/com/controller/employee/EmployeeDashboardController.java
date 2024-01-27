package com.controller.employee;

import com.entities.Employee;
import com.hms.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeDashboardController implements Initializable{

    @FXML
    public Label greetLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SessionManager<Employee> empSession =  SessionManager.getInstance();
        Employee employee = empSession.getCurrentUser();

        if (employee != null) {
            String fullName = employee.getName();
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
