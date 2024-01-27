package com.controller.employee;

import com.dao.EmployeeDao;
import com.entities.Employee;
import com.hms.Main;
import com.hms.SessionManager;
import com.utility.ConnectionProvider;
import com.utility.HMS_Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class EmployeeLoginController {
    @FXML
    private TextField userId;
    @FXML
    private PasswordField password;
    @FXML
    public Button loginButton;
    @FXML
    protected Label message;

    @FXML
    void handleLoginButton(ActionEvent event) {
        if (userId.getText().equals("") || password.getText().equals("")) {
            message.setVisible(true);
            message.setText("Username or Password cannot be empty");
            return;
        }

        Connection con = ConnectionProvider.getConnection();
        EmployeeDao empDao = new EmployeeDao(con);

        int empId = Integer.parseInt(this.userId.getText());
        String pwd = password.getText();
        Employee emp = empDao.getEmployee(empId, pwd);

        if (emp == null) {
            message.setVisible(true);
            message.setText("Invalid UserId or Password");
        } else {
            try {
                // Initialize SessionManager for Admin and set current admin
                SessionManager empSession = SessionManager.getInstance();
                empSession.setCurrentUser(emp);

                // Load the CustomerDashboard.fxml file using the FXMLLoader
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Employee/EmployeeDashboard.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle("Employee Dashboard");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                message.setText("Error occurred, please try again");
                message.setVisible(true);
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleForgetPassword(ActionEvent event) {

        HMS_Utility.loadScene(event, "/com/hms/ForgotPassword.fxml", "Forget Password" );

    }
}

