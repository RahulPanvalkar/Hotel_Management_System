package com.controller.admin;

import com.dao.AdminDao;
import com.entities.Admin;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class AdminLoginController {

    @FXML
    private TextField userId;
    @FXML
    private PasswordField password;
    @FXML
    public Button loginButton;
    @FXML
    private Label message;
    @FXML
    private Button user;

    public void onLoginButtonClicked(ActionEvent event) {
        if (userId.getText().isEmpty() || password.getText().isEmpty()) {
            message.setVisible(true);
            message.setText("Username or Password cannot be empty");
            return;
        }

        Connection con = ConnectionProvider.getConnection();
        if (con == null){
            message.setVisible(true);
            message.setText("Something went wrong, please try again");
            return;
        }
        AdminDao aDao = new AdminDao(con);

        int adminId = Integer.parseInt(this.userId.getText());
        String pwd = password.getText();
        Admin admin = aDao.getAdmin(adminId, pwd);

        if (admin == null) {
            message.setVisible(true);
            message.setText("Invalid UserId or Password");
        } else {
            try {
                // Initialize SessionManager for Admin and set current admin
                SessionManager adminSession = SessionManager.getInstance();
                adminSession.setCurrentUser(admin);

                // Load the AdminDashboard.fxml file using the FXMLLoader
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Admin/AdminDashboard.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle("Admin Dashboard");
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
