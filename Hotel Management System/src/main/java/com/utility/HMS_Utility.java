package com.utility;

import com.dao.CustomerDao;
import com.dao.EmployeeDao;
import com.entities.Customer;
import com.entities.Employee;
import com.entities.Rooms;
import com.hms.Main;
import com.hms.SessionManager;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.dao.RoomDao;

import java.io.IOException;
import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HMS_Utility {
    public static void logout(MouseEvent event, SessionManager session){
        Alert alert = new Alert(Alert.AlertType.NONE, "Do you want to Log out?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Log Out");
        alert.getDialogPane().setPrefSize(250, 90);

        if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            session.endSession();
            loadScene(event, "/com/hms/Home.fxml","Home");
        }
    }

    public static void loadScene(Event event, String resource, String title){
        System.out.println("Inside HMS_Utility.loadStage..");
        try {
            Parent root = FXMLLoader.load(Main.class.getResource(resource));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadNewStage(String resource, String title){
        System.out.println("Inside HMS_Utility.loadNewStage..");
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(resource));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void limitTextFieldLength(TextField field,int maxLength){
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > maxLength) {
                field.setText(newValue.substring(0, maxLength));
            }
        });
    }

    public static void limitPasswordFieldLength(PasswordField field,int maxLength){
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > maxLength) {
                field.setText(newValue.substring(0, maxLength));
            }
        });
    }

    public static <T> void handleDeleteRow(T dataObj){
        String className = dataObj.getClass().getSimpleName();
        Connection con = ConnectionProvider.getConnection();
        if(className.equals("Employee")){
            EmployeeDao dao = new EmployeeDao(con);
            if (dao.deleteEmployee((Employee) dataObj)){
                System.out.println("Employee Successfully Deleted!");
            }else{
                System.out.println("Error occurred!");
            }
        }else if(className.equals("Customer")){
            CustomerDao dao = new CustomerDao(con);
            if (dao.deleteCustomer((Customer) dataObj)){
                System.out.println("Customer Successfully Deleted!");
            }else{
                System.out.println("Error occurred!");
            }
        }else if(className.equals("Rooms")){
            RoomDao dao = new RoomDao(con);
            if (dao.deleteRoom((Rooms) dataObj)){
                System.out.println("Room Successfully Deleted!");
            }else{
                System.out.println("Error occurred!");
            }
        }

    }


    public static boolean mobileNumberValidation(String mobNumber) {
        System.out.println("Inside mobileNumberValidation => mobNumber : "+mobNumber);
        return mobNumber.matches("\\d+");
    }

    public static boolean emailValidation(String email) {
        System.out.println("Inside emailrValidation => email : "+email);

        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$";

        // Compile the pattern
        Pattern pattern = Pattern.compile(emailRegex);

        // Create a matcher with the email string
        Matcher matcher = pattern.matcher(email);

        // Check if the pattern is found in the string
        return matcher.matches();
    }


}
