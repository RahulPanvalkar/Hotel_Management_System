package com.controller.customer;

import com.dao.CustomerDao;
import com.entities.Customer;
import com.hms.Main;
import com.utility.ConnectionProvider;
import com.utility.HMS_Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class CustomerRegistrationController {
    @FXML
    private TextField fullName;
    @FXML
    private TextField email;
    @FXML
    private TextField mobileNumber;
    @FXML
    private TextField address;
    @FXML
    private PasswordField password1;
    @FXML
    private PasswordField password2;
    @FXML
    private Label message;

    public void handleSignUpButton() {

        String custName = fullName.getText();
        String custEmail = email.getText();
        String custMobile = mobileNumber.getText();
        String custAddress = address.getText();
        String custPass1 = password1.getText();
        String custPass2 = password2.getText();

        if (custName.isEmpty() || custEmail.isEmpty() || custPass1.isEmpty() ||
                custPass2.isEmpty() || custMobile.isEmpty() || custAddress.isEmpty()){
            message.setText("Field Cannot be empty");
            return;
        }else if (!custPass1.equals(custPass2)){
            message.setText("Password does not match");
            return;
        }

        Customer customer = new Customer(custName, custEmail, custMobile, custPass1, custAddress);

        Connection con = ConnectionProvider.getConnection();
        CustomerDao customerDao = new CustomerDao(con);

        boolean isAdded = customerDao.addCustomer(customer);
        if (isAdded){
            message.setText("Registration Successful!");
            fullName.setText("");
            email.setText("");
            password1.setText("");
            password2.setText("");
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
        } else if (textField.equals(password1)) {
            int userNameMaxLength = 20;
            HMS_Utility.limitPasswordFieldLength(password1, userNameMaxLength);
        }else if (textField.equals(password2)) {
            int userNameMaxLength = 20;
            HMS_Utility.limitPasswordFieldLength(password2, userNameMaxLength);
        } else {
            throw new IllegalStateException("Unexpected value: " + textField);
        }
    }

    public void handleBackButton(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/Customer/CustomerLogin.fxml"));
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setTitle("Customer Login");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
