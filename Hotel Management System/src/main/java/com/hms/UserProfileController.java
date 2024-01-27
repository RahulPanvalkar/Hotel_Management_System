package com.hms;

import com.dao.AdminDao;
import com.dao.EmployeeDao;
import com.entities.Admin;
import com.entities.Employee;
import com.entities.Customer;
import com.utility.ConnectionProvider;
import com.utility.HMS_Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.sql.Connection;
import java.net.URL;
import java.util.ResourceBundle;

public class UserProfileController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField mobileField;

    @FXML
    private Label idLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label mobileLabel;

    @FXML
    private Button editButton;
    @FXML
    private Button saveButton;

    @FXML
    private Button closeButton;
    @FXML
    private Button cancelButton;

    @FXML
    private Label message;

    String userClass = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        SessionManager session = SessionManager.getInstance();
        var user = session.getCurrentUser();

        if (user == null) {
            System.out.println("user is null");
            return;
        }

        userClass = user instanceof Admin ? "Admin" : (user instanceof Employee ? "Employee" : "Customer");
        System.out.println("userClass : [" + userClass + "]");

        if (userClass.equalsIgnoreCase("Admin")) {
            setValuesForAdmin((Admin)user);
        }else if (userClass.equalsIgnoreCase("Employee")) {
            setValuesForEmployee((Employee)user);
        }else if (userClass.equalsIgnoreCase("Customer")) {
            setValuesForCustomer((Customer)user);
        }

    }

    private void setValuesForAdmin(Admin user) {
        idLabel.setText(String.valueOf(user.getId()));
        nameLabel.setText(user.getName());
        emailLabel.setText(user.getEmail());
        mobileLabel.setText(user.getMobNumber());

        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
        mobileField.setText(user.getMobNumber());
    }

    private void setValuesForEmployee(Employee user) {
        idLabel.setText(String.valueOf(user.getEmpId()));
        nameLabel.setText(user.getName());
        emailLabel.setText(user.getEmail());
        mobileLabel.setText(user.getMobNumber());

        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
        mobileField.setText(user.getMobNumber());
    }

    private void setValuesForCustomer(Customer user) {
        idLabel.setText(String.valueOf(user.getCustomerId()));
        nameLabel.setText(user.getName());
        emailLabel.setText(user.getEmail());
        mobileLabel.setText(user.getMobileNum());

        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
        mobileField.setText(user.getMobileNum());
    }

    public void handleKeyTyped(KeyEvent event) {
        TextField textField = (TextField) event.getSource();

        if (textField.equals(nameField)) {
            int fullNameMaxLength = 50;
            HMS_Utility.limitTextFieldLength(nameField, fullNameMaxLength);
        } else if (textField.equals(emailField)) {
            int emailMaxLength = 30;
            HMS_Utility.limitTextFieldLength(emailField, emailMaxLength);
        } else if (textField.equals(mobileField)) {
            int mobileNumberMaxLength = 10;
            HMS_Utility.limitTextFieldLength(mobileField, mobileNumberMaxLength);
        } else {
            throw new IllegalStateException("Unexpected value: " + textField);
        }
    }

    public void handleEditButtonClicked(ActionEvent event) {
        System.out.println("Inside handleEditButtonClicked...");
        nameLabel.setVisible(false);
        nameField.setVisible(true);
        emailLabel.setVisible(false);
        emailField.setVisible(true);
        mobileLabel.setVisible(false);
        mobileField.setVisible(true);

        editButton.setVisible(false);
        saveButton.setVisible(true);

        closeButton.setVisible(false);
        cancelButton.setVisible(true);
    }

    public void handleCloseButtonClicked(ActionEvent event) {
        System.out.println("Inside handleCloseButtonClicked...");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void handleSaveButtonClicked(ActionEvent event) {
        int id = Integer.parseInt(idLabel.getText());

        String updatedName = nameField.getText();
        System.out.println("updatedName : "+updatedName);
        String updatedEmail = emailField.getText();
        System.out.println("updatedEmail : "+updatedEmail);
        String updatedMobile = mobileField.getText();
        System.out.println("updatedMobile : "+updatedMobile);

        if ((updatedName == null || updatedEmail == null || updatedMobile == null) ||
                (updatedName.isEmpty() || updatedEmail.isEmpty() || updatedMobile.isEmpty())){
            message.setVisible(true);
            return;
        }

        if (userClass.equalsIgnoreCase("Admin")) {
            Admin admin = new Admin(id, nameField.getText(), emailField.getText(), mobileField.getText());

            Connection con = ConnectionProvider.getConnection();
            AdminDao adminDao = new AdminDao(con);

            boolean isUpdated = adminDao.updateAdmin(admin);

            if (isUpdated){
                admin = adminDao.getAdmin(admin.getId());
                SessionManager adminSession = SessionManager.getInstance();
                adminSession.setCurrentUser(admin);

                HMS_Utility.loadScene(event, "/com/hms/UserProfile.fxml", "User Profile");
            }else{
                message.setText("Something went wrong, please try again");
            }
        }else if (userClass.equalsIgnoreCase("Employee")) {
            Employee employee = new Employee(id, nameField.getText(), emailField.getText(), mobileField.getText());

            Connection con = ConnectionProvider.getConnection();
            EmployeeDao employeeDao = new EmployeeDao(con);

            boolean isUpdated = employeeDao.updateEmployee(employee);

            if (isUpdated){
                employee = employeeDao.getEmployee(employee.getEmpId());
                SessionManager empSession = SessionManager.getInstance();
                empSession.setCurrentUser(employee);

                HMS_Utility.loadScene(event, "/com/hms/UserProfile.fxml", "User Profile");
            }else{
                message.setText("Something went wrong, please try again");
            }
        }else if (userClass.equalsIgnoreCase("Customer")) {
            Admin admin = new Admin(id, nameField.getText(), emailField.getText(), mobileField.getText());

            Connection con = ConnectionProvider.getConnection();
            AdminDao adminDao = new AdminDao(con);

            boolean isUpdated = adminDao.updateAdmin(admin);

            if (isUpdated){
                admin = adminDao.getAdmin(admin.getId());
                SessionManager adminSession = SessionManager.getInstance();
                adminSession.setCurrentUser(admin);

                HMS_Utility.loadScene(event, "/com/hms/UserProfile.fxml", "User Profile");
            }else{
                message.setText("Something went wrong, please try again");
            }
        }

    }

    public void handleCancelButtonClicked(ActionEvent event) {
        System.out.println("Cancel Button clicked!");
        HMS_Utility.loadScene(event,"/com/hms/UserProfile.fxml","");
    }
}
