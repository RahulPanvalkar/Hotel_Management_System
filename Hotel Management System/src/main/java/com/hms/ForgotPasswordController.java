package com.hms;

import com.dao.AdminDao;
import com.dao.CustomerDao;
import com.dao.EmployeeDao;
import com.utility.ConnectionProvider;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import com.utility.HMS_Utility;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class ForgotPasswordController implements Initializable {
    @FXML
    private TextField mobileNumber;
    @FXML
    private PasswordField password1;
    @FXML
    private PasswordField password2;
    @FXML
    private Label message;
    @FXML
    private Label errorMessage;
    @FXML
    private AnchorPane validateMobile;
    @FXML
    private AnchorPane resetPassword;
    @FXML
    private RadioButton showPassword;

    String currentUserType;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SessionManager session = SessionManager.getInstance();
        currentUserType = session.getCurrentUserType();
        System.out.println("Current User Type: "+ currentUserType);
    }

    @FXML
    public void handleSubmitClicked() {
        Connection con = ConnectionProvider.getConnection();

        if(currentUserType.equalsIgnoreCase("Admin")){
            AdminDao adminDao = new AdminDao(con);
            if (mobileNumber.getText().isEmpty() || !adminDao.validateMobile(mobileNumber.getText())) {
                message.setVisible(true);
                return;
            }
        } else if(currentUserType.equalsIgnoreCase("Employee")){
            EmployeeDao empDao = new EmployeeDao(con);
            if (mobileNumber.getText().isEmpty() || !empDao.validateMobile(mobileNumber.getText())) {
                message.setVisible(true);
                return;
            }
        } else if (currentUserType.equalsIgnoreCase("Customer")) {
            CustomerDao custDao = new CustomerDao(con);
            if (mobileNumber.getText().isEmpty() || !custDao.validateMobile(mobileNumber.getText())) {
                message.setVisible(true);
                return;
            }
        }
        validateMobile.setVisible(false);
        resetPassword.setVisible(true);
    }

    public void handleResetKeyPressed() {
        Connection con = ConnectionProvider.getConnection();


        if (password1.getText().isEmpty() || password2.getText().isEmpty()){
            errorMessage.setText("Empty Password Field");
            errorMessage.setVisible(true);
            return;
        }
        if (!password1.getText().equals(password2.getText()) ){
            errorMessage.setText("Passwords do not match");
            errorMessage.setVisible(true);
            return;
        }
        boolean hasChanged = false;
        if (currentUserType.equalsIgnoreCase("Admin")) {
            AdminDao adminDao = new AdminDao(con);
            hasChanged = adminDao.changePassword(password1.getText(), mobileNumber.getText());
        } else if (currentUserType.equalsIgnoreCase("Employee")) {
            EmployeeDao empDao = new EmployeeDao(con);
            hasChanged = empDao.changePassword(password1.getText(), mobileNumber.getText());
        } else if (currentUserType.equalsIgnoreCase("Customer")) {
            CustomerDao custDao = new CustomerDao(con);
            hasChanged = custDao.changePassword(password1.getText(), mobileNumber.getText());
        }
        if (hasChanged){
            errorMessage.setTextFill(Paint.valueOf("Green"));
            errorMessage.setText("Password has been successfully reset.");
            errorMessage.setVisible(true);
            password1.setText("");
            password2.setText("");
        }else{
            errorMessage.setText("Something went wrong, please try again");
            errorMessage.setVisible(true);
        }
    }

    public void handleShowPassword() {
        if (showPassword.isSelected()) {
            password1.setPromptText(password1.getText());
            password2.setPromptText(password2.getText());
            password1.setText("");
            password2.setText("");
        } else {
            password1.setText(password1.getPromptText());
            password2.setText(password2.getPromptText());
            password1.setPromptText("");
            password2.setPromptText("");
        }
    }

    @FXML
    public void handleKeyTyped(KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        if (textField.equals(mobileNumber)) {
            int mobileMaxLength = 10;
            HMS_Utility.limitTextFieldLength(mobileNumber, mobileMaxLength);
        } else if (textField.equals(password1)) {
            int passwordMaxLength = 20;
            HMS_Utility.limitPasswordFieldLength(password1, passwordMaxLength);
        }else if (textField.equals(password2)) {
            int passwordMaxLength = 20;
            HMS_Utility.limitPasswordFieldLength(password2, passwordMaxLength);
        }
    }
}
