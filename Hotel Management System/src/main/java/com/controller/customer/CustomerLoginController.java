package com.controller.customer;

import com.dao.CustomerDao;
import com.entities.Customer;
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

public class CustomerLoginController {
    @FXML
    private TextField userId;
    @FXML
    private PasswordField password;
    @FXML
    private Label message;

    @FXML
    void handleLoginButton(ActionEvent event) {
        if (userId.getText().equals("") || password.getText().equals("")) {
            message.setVisible(true);
            message.setText("Username or Password cannot be empty");
            return;
        }

        String custId = userId.getText();
        String pwd = password.getText();

        String credentialType;

        // Determine the credential type based on the input format (Mobile number or email)
        if (custId.matches("\\d+")) {
            credentialType = "mobNumber";
        } else {
            credentialType = "email";
        }
        System.out.println("Credential Type : "+credentialType);

        Connection con = ConnectionProvider.getConnection();
        CustomerDao custDao = new CustomerDao(con);
        Customer customer = custDao.getCustomer(custId, pwd, credentialType);

        if (customer == null){
            message.setText("Invalid UserId or Password");
            message.setVisible(true);
        } else{
            try {
                // Initialize SessionManager for Admin and set current admin
                SessionManager custSession = SessionManager.getInstance();
                custSession.setCurrentUser(customer);

                // Load the CustomerDashboard.fxml file using the FXMLLoader
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Customer/CustomerDashboard.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle("Customer Dashboard");
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
    void onRegisterButton(ActionEvent event) {

        HMS_Utility.loadScene(event, "/Customer/CustomerRegistration.fxml", "Customer Registration" );

    }

    @FXML
    public void handleForgetPassword(ActionEvent event) {

        HMS_Utility.loadScene(event, "/com/hms/ForgotPassword.fxml", "Forget Password" );

    }

}
